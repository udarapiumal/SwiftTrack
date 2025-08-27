package com.example.ros.Service;

import com.example.ros.Config.RabbitConfig;
import com.example.ros.Dto.RouteRequest;
import com.example.ros.Dto.RouteResponse;
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
        // Simulate long-running route calculation
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        RouteResponse response = new RouteResponse();
        response.setOrderId(request.getOrderId());
        response.setDriverId(request.getDriverId());
        response.setRoute("Optimized route: " + String.join(" -> ", request.getAddresses()));
        response.setStatus("SUCCESS");

        // Publish route updated event
        rabbitTemplate.convertAndSend(RabbitConfig.ROUTE_UPDATED_QUEUE, response);

        return CompletableFuture.completedFuture(response);
    }
}
