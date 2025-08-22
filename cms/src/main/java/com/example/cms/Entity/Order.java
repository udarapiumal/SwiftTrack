package com.example.cms.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    @ManyToOne
    private Client client;

    private String items;
    private String status; // e.g., "SUBMITTED", "COMPLETED", "CANCELLED"

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
