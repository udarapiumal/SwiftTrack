package com.example.cms.DTO;

import java.io.Serializable;

public class OrderCanceledEvent implements Serializable {
    private String orderId;

    public OrderCanceledEvent() {}

    public OrderCanceledEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
