package com.example.cms.Service;

import com.example.cms.Client.CmsSoapClient;
import com.example.cms.Entity.Client;
import com.example.cms.Entity.Order;
import com.example.cms.Messaging.OrderEventPublisher;
import com.example.cms.Repository.ClientRepository;
import com.example.cms.Repository.OrderRepository;
import com.example.cms.generated.GetClientDetailsResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsClientService {

    private final CmsSoapClient cmsSoapClient;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    private final OrderEventPublisher orderEventPublisher;

    public CmsClientService(CmsSoapClient cmsSoapClient,
                            OrderRepository orderRepository,
                            ClientRepository clientRepository,
                            OrderEventPublisher orderEventPublisher) {
        this.cmsSoapClient = cmsSoapClient;
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.orderEventPublisher = orderEventPublisher;  // ✅ inject publisher
    }

    // Submit Order
    public String submitOrder(String orderId, String clientId, String items) {
        // existing logic: save client/order to DB
        Client client = clientRepository.findById(clientId).orElseGet(() -> {
            Client newClient = new Client();
            newClient.setClientId(clientId);
            newClient.setClientName("Client " + clientId);
            newClient.setEmail(clientId + "@example.com");
            newClient.setPhone("+94-700-000-000");
            return clientRepository.save(newClient);
        });

        Order order = new Order();
        order.setOrderId(orderId);
        order.setClient(client);
        order.setItems(items);
        order.setStatus("SUBMITTED");
        orderRepository.save(order);

        // ✅ publish event
        orderEventPublisher.publishOrderCreated(orderId, clientId, items);

        return "Order " + orderId + " submitted successfully for client " + clientId;
    }


    // Cancel Order
    public String cancelOrder(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("CANCELLED");
            orderRepository.save(order);

            // ✅ publish cancel event
            orderEventPublisher.publishOrderCanceled(orderId);

            return "Order " + orderId + " has been canceled";
        } else {
            return "Order " + orderId + " not found";
        }
    }


    // Get Order Status
    public String getOrderStatus(String orderId){
        return orderRepository.findById(orderId)
                .map(Order::getStatus)
                .orElse("Order " + orderId + " not found");
    }

    // Get Client Details
    public GetClientDetailsResponse getClientDetails(String clientId){
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        GetClientDetailsResponse response = new GetClientDetailsResponse();
        response.setClientName(client.getClientName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        return response;
    }

}
