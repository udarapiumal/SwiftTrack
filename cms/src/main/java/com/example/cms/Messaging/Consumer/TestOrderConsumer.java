package com.example.cms.Messaging.Consumer;

import com.example.cms.Config.RabbitConfig;
import com.example.cms.Event.OrderCreatedEvent;
import com.example.cms.Event.OrderCanceledEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TestOrderConsumer {

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("✅ TestConsumer: Order Created Event received -> " + event.getOrderId());
    }

    @RabbitListener(queues = RabbitConfig.ORDER_CANCELED_QUEUE)
    public void handleOrderCanceled(OrderCanceledEvent event) {
        System.out.println("✅ TestConsumer: Order Canceled Event received -> " + event.getOrderId());
    }
}
