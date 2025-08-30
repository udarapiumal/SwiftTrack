package com.example.ros.Service;

import com.example.ros.Config.RabbitConfig;
import com.example.ros.Dto.RouteRequest;
import com.example.ros.Dto.RouteResponse;
import com.example.ros.Dto.PackageUpdate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RouteService {

    private final RabbitTemplate rabbitTemplate;

    public RouteService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Async
    public CompletableFuture<RouteResponse> optimizeRoute(RouteRequest request) {
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        RouteResponse response = new RouteResponse();
        response.setOrderId(request.getOrderId());
        response.setDriverId(request.getDriverId());
        response.setRoute("Optimized route: " + (request.getAddresses() != null ? String.join(" -> ", request.getAddresses()) : "No addresses"));
        response.setStatus("SUCCESS");

        rabbitTemplate.convertAndSend(RabbitConfig.ROUTE_UPDATED_QUEUE, response);

        return CompletableFuture.completedFuture(response);
    }

    public void handlePackageUpdate(PackageUpdate update) {
        System.out.println("📦 Handling WMS package: " + update.getPackageId() + " | Status: " + update.getStatus());
        // You can extend this if needed
    }
}
