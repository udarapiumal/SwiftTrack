package com.example.wms.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public class PackageUpdate {
    private String packageId;
    private String orderId;
    private String status; // e.g., RECEIVED, LOADED, DELIVERED
    private LocalDateTime timestamp;

    public PackageUpdate() {
    }


    public PackageUpdate(String packageId, String orderId, String status, LocalDateTime timestamp) {
        this.packageId = packageId;
        this.orderId = orderId;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
