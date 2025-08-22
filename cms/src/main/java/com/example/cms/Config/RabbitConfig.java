package com.example.cms.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_CREATED_QUEUE = "order.created";
    public static final String ORDER_UPDATED_QUEUE = "order.updated";
    public static final String ORDER_CANCELED_QUEUE = "order.canceled";
    public static final String PACKAGE_STATUS_UPDATED_QUEUE = "package.status.updated";

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue orderUpdatedQueue() {
        return new Queue(ORDER_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue orderCanceledQueue() {
        return new Queue(ORDER_CANCELED_QUEUE, true);
    }

    @Bean
    public Queue packageStatusUpdatedQueue() {
        return new Queue(PACKAGE_STATUS_UPDATED_QUEUE, true);
    }
}
