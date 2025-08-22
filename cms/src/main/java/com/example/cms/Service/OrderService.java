package com.example.cms.Service;

import com.example.cms.Entity.Client;
import com.example.cms.Entity.Order;
import com.example.cms.Repository.OrderRepository;
import com.example.cms.Repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Order submitOrder(String orderId, String clientId, String items) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Order order = new Order();
        order.setOrderId(orderId);
        order.setClient(client);
        order.setItems(items);
        order.setStatus("SUBMITTED");

        return orderRepository.save(order);  // <-- This actually inserts the row
    }

}
