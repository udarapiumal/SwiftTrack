package com.example.wms.Messaging;

import com.example.wms.Config.RabbitConfig;
import com.example.wms.Dto.PackageUpdate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class PackageUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitTemplate jsonRabbitTemplate;

    public PackageUpdatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        // Create a separate RabbitTemplate specifically for JSON publishing
        this.jsonRabbitTemplate = new RabbitTemplate(rabbitTemplate.getConnectionFactory());
        this.jsonRabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void publishUpdate(PackageUpdate update) {
        // Use the JSON-configured template for publishing PackageUpdate objects
        jsonRabbitTemplate.convertAndSend(RabbitConfig.PACKAGE_UPDATED_QUEUE, update);
        System.out.println("📦 Published package update: " + update.getPackageId() + " -> " + update.getStatus());
    }
}