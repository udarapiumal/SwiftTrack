package com.example.wms.Messaging;

import com.example.wms.Config.RabbitConfig;
import com.example.wms.Dto.PackageUpdate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PackageUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;

    public PackageUpdatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUpdate(PackageUpdate update) {
        rabbitTemplate.convertAndSend(RabbitConfig.PACKAGE_UPDATED_QUEUE, update);
        System.out.println("📦 Published package update: " + update.getPackageId() + " -> " + update.getStatus());
    }
}
