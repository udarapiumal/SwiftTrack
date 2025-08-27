package com.example.ros.Messaging;

import com.example.ros.Dto.RouteRequest;
import com.example.ros.Service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Service
public class CmsOrderConsumer {

    private final RouteService routeService;
    private final ObjectMapper objectMapper;

    public CmsOrderConsumer(RouteService routeService) {
        this.routeService = routeService;
        this.objectMapper = new ObjectMapper();
    }

    // Use the rawRabbitListenerContainerFactory to avoid class resolution errors
    @RabbitListener(
            queues = "order.created",
            containerFactory = "rawRabbitListenerContainerFactory" // <-- use raw container
    )
    public void handleOrderCreated(byte[] payload) {
        try {
            Map<String, Object> event = objectMapper.readValue(payload, Map.class);
            System.out.println("✅ Received CMS OrderCreatedEvent: " + event);

            RouteRequest request = new RouteRequest();
            request.setOrderId((String) event.getOrDefault("orderId", "unknown"));
            request.setDriverId("driver123");

            Object items = event.get("items");
            if (items != null && !items.toString().isEmpty()) {
                request.setAddresses(Arrays.asList(items.toString().split(",")));
            }

            routeService.optimizeRoute(request)
                    .thenAccept(response -> System.out.println("Optimized route -> " + response.getRoute()));

        } catch (Exception e) {
            System.err.println("❌ Failed to process OrderCreatedEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
