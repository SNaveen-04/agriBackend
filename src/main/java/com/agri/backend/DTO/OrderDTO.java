package com.agri.backend.DTO;

import java.time.LocalDateTime;

public class OrderDTO {
    private long id;
    private UserDTO user;
    private LocalDateTime orderTimestamp;
    private String status;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
	public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }
    public void setOrderTimestamp(LocalDateTime orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
