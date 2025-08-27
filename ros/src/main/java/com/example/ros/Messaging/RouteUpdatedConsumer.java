package com.example.ros.Messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RouteUpdatedConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(
            queues = "route.updated",
            containerFactory = "rawRabbitListenerContainerFactory" // same raw container
    )
    public void handleRouteUpdated(byte[] payload) {
        try {
            Map<String, Object> event = objectMapper.readValue(payload, Map.class);
            System.out.println("✅ Received RouteUpdatedEvent: " + event);

            // You can add your processing logic here

        } catch (Exception e) {
            System.err.println("❌ Failed to process RouteUpdatedEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
