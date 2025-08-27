package com.example.ros.Messaging;

import com.example.ros.Dto.RouteRequest;
import com.example.ros.Service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class CmsOrderConsumer {

    private final RouteService routeService;
    private final ObjectMapper objectMapper;

    public CmsOrderConsumer(RouteService routeService) {
        this.routeService = routeService;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = "order.created")
    public void handleOrderCreated(byte[] payload) throws Exception {
        // Deserialize the payload
        Map<String, Object> event = objectMapper.readValue(payload, Map.class);

        System.out.println("✅ Received CMS OrderCreatedEvent: " + event);

        // Map CMS event fields to RouteRequest
        RouteRequest request = new RouteRequest();
        request.setOrderId((String) event.get("orderId"));
        request.setDriverId("driver123");
        request.setAddresses(Arrays.asList(((String) event.get("items")).split(",")));

        // Call route optimization
        routeService.optimizeRoute(request).thenAccept(response ->
                System.out.println("Optimized route -> " + response.getRoute())
        );
    }
}
