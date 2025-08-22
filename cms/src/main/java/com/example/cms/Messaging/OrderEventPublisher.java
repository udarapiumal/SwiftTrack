package com.example.cms.Messaging;

import com.example.cms.Config.RabbitConfig;
import com.example.cms.Event.OrderCanceledEvent;
import com.example.cms.Event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderCreated(String orderId, String clientId, String items) {
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, clientId, items);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_CREATED_QUEUE, event);
    }

    public void publishOrderCanceled(String orderId) {
        OrderCanceledEvent event = new OrderCanceledEvent(orderId);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_CANCELED_QUEUE, event);
    }
}