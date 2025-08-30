package com.example.ros.Messaging;

import com.example.ros.Dto.RouteRequest;
import com.example.ros.Service.PackageStatusService;
import com.example.ros.Service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CmsOrderConsumer {

    private final RouteService routeService;
    private final PackageStatusService packageStatusService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CmsOrderConsumer(RouteService routeService, PackageStatusService packageStatusService) {
        this.routeService = routeService;
        this.packageStatusService = packageStatusService;
    }

    @RabbitListener(queues = "order.created", containerFactory = "rawRabbitListenerContainerFactory")
    public void handleOrderCreated(byte[] payload) {
        try {
            Map<String, Object> event = objectMapper.readValue(payload, Map.class);
            System.out.println("✅ ROS Received OrderCreatedEvent: " + event);

            String orderId = (String) event.getOrDefault("orderId", "unknown");
            Object items = event.get("items");
            List<String> itemList = items != null ? Arrays.asList(items.toString().split(",")) : List.of();

            // Generate package IDs that match WMS format: orderId + "-PKG" + (i + 1)
            List<String> packageIds = new ArrayList<>();
            for (int i = 0; i < itemList.size(); i++) {
                packageIds.add(orderId + "-PKG" + (i + 1));
            }

            packageStatusService.registerPackages(orderId, packageIds);
            System.out.println("📋 ROS registered packages for tracking: " + packageIds);

        } catch (Exception e) {
            System.err.println("❌ ROS Failed to process OrderCreatedEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
