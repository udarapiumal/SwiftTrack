package com.example.wms.Dto;

import java.io.Serializable;

public class OrderCreatedEvent implements Serializable {
    private String orderId;
    private String clientId;
    private String items;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(String orderId, String clientId, String items) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.items = items;
    }

    // Getters and Setters (matching CMS exactly)
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", items='" + items + '\'' +
                '}';
    }
}