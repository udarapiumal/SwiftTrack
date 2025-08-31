package com.example.wms.Messaging;

import com.example.wms.Config.RabbitConfig;
import com.example.wms.Dto.PackageUpdate;
import com.example.wms.Service.WmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class WmsListener {

    private final WmsService wmsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WmsListener(WmsService wmsService) {
        this.wmsService = wmsService;
    }

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE, containerFactory = "rawRabbitListenerContainerFactory")
    public void handleOrderCreated(byte[] payload) {
        try {
            Map<String, Object> event = objectMapper.readValue(payload, Map.class);
            System.out.println("✅ WMS Received OrderCreatedEvent: " + event);

            String orderId = (String) event.get("orderId");
            String items = (String) event.get("items");

            // Parse items and process the order
            List<String> itemList = Arrays.asList(items.split(","));
            wmsService.processOrder(orderId, itemList);

            System.out.println("📦 WMS processed order: " + orderId + " with " + itemList.size() + " items");

        } catch (Exception e) {
            System.err.println("❌ WMS Failed to process OrderCreatedEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "order.created.wms", containerFactory = "rawRabbitListenerContainerFactory")
    public void handlePackageUpdate(byte[] payload) {
        try {
            Map<String, Object> update = objectMapper.readValue(payload, Map.class);
            System.out.println("📥 WMS Received package update: " + update.get("packageId")
                    + " | Order: " + update.get("orderId")
                    + " | Status: " + update.get("status"));
            // Optional: update DB, notify client portal, etc.
        } catch (Exception e) {
            System.err.println("❌ WMS Failed to process PackageUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}