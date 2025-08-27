package com.example.ros.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_CREATED_QUEUE = "order.created";
    public static final String ROUTE_UPDATED_QUEUE = "route.updated";

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue routeUpdatedQueue() {
        return new Queue(ROUTE_UPDATED_QUEUE, true);
    }

    // Use simple converter to receive raw byte[]
    @Bean
    public MessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rawRabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                  MessageConverter simpleMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(simpleMessageConverter); // no type mapping
        return factory;
    }
}
