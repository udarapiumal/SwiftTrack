package com.example.cms.Endpoint;

import com.example.cms.Entity.Client;
import com.example.cms.Entity.Order;
import com.example.cms.Repository.ClientRepository;
import com.example.cms.Repository.OrderRepository;
import com.example.cms.generated.*;
import jakarta.jws.WebService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@WebService(
        endpointInterface = "com.example.cms.generated.CmsPort",
        serviceName = "CmsService",
        portName = "CmsPort",
        targetNamespace = "http://example.com/cms"
)
@Service
public class CmsSoapEndpoint implements CmsPort {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    // Inject repositories via constructor
    public CmsSoapEndpoint(OrderRepository orderRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) {
        SubmitOrderResponse response = new SubmitOrderResponse();

        // Find client
        Optional<Client> clientOpt = clientRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            response.setStatus("Client " + request.getClientId() + " not found!");
            return response;
        }

        Client client = clientOpt.get();

        // Create order
        Order order = new Order();
        order.setOrderId(request.getOrderId());
        order.setClient(client);
        order.setItems(request.getItems());
        order.setStatus("SUBMITTED");

        orderRepository.save(order);
        response.setStatus("Order " + request.getOrderId() + " submitted successfully for client " + client.getClientId());
        return response;
    }

    @Override
    public GetOrderStatusResponse getOrderStatus(GetOrderStatusRequest request) {
        GetOrderStatusResponse response = new GetOrderStatusResponse();

        Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());
        if (orderOpt.isPresent()) {
            response.setStatus(orderOpt.get().getStatus());
        } else {
            response.setStatus("Order " + request.getOrderId() + " not found!");
        }
        return response;
    }

    @Override
    @Transactional
    public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
        CancelOrderResponse response = new CancelOrderResponse();

        Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            response.setStatus("Order " + request.getOrderId() + " has been canceled.");
        } else {
            response.setStatus("Order " + request.getOrderId() + " not found!");
        }
        return response;
    }

    @Override
    public GetClientDetailsResponse getClientDetails(GetClientDetailsRequest request) {
        GetClientDetailsResponse response = new GetClientDetailsResponse();

        Optional<Client> clientOpt = clientRepository.findById(request.getClientId());
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            response.setClientName(client.getClientName());
            response.setEmail(client.getEmail());
            response.setPhone(client.getPhone());
        } else {
            response.setClientName("Client not found");
            response.setEmail("");
            response.setPhone("");
        }

        return response;
    }
}
