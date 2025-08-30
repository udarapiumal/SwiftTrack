package com.example.ros.Messaging;

import com.example.ros.Dto.RouteRequest;
import com.example.ros.Service.PackageStatusService;
import com.example.ros.Service.RouteService;
import com.example.ros.Dto.PackageUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PackageUpdateConsumer {

    private final RouteService routeService;
    private final PackageStatusService packageStatusService;
    private final ObjectMapper objectMapper;

    public PackageUpdateConsumer(RouteService routeService, PackageStatusService packageStatusService) {
        this.routeService = routeService;
        this.packageStatusService = packageStatusService;

        // Configure ObjectMapper with JSR310 support
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @RabbitListener(queues = "package.updated", containerFactory = "rawRabbitListenerContainerFactory")
    public void handlePackageUpdated(byte[] payload) {
        try {
            PackageUpdate update = objectMapper.readValue(payload, PackageUpdate.class);
            System.out.println("📥 Received package update: " + update.getPackageId() + " | Order: " + update.getOrderId() + " | Status: " + update.getStatus());

            boolean allReady = packageStatusService.markPackageReady(update.getOrderId(), update.getPackageId());
            if (allReady) {
                RouteRequest request = new RouteRequest();
                request.setOrderId(update.getOrderId());
                request.setDriverId("driver123");
                routeService.optimizeRoute(request)
                        .thenAccept(response -> System.out.println("Optimized route -> " + response.getRoute()));
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to process PackageUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}