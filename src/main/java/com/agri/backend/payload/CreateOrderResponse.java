package com.agri.backend.payload;

public class CreateOrderResponse {
    private String orderId;

    public CreateOrderResponse(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
