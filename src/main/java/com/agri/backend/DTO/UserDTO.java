package com.agri.backend.DTO;

import java.util.List;
import java.util.Map;

import com.agri.backend.entity.OrderItem.OrderItemStatus;

public class UserDTO {
    private long id;
    private String userName;
    private String email;
    private String number;
    private String userType;
    private List<String> addresses;
    private Map<Long,OrderItemStatus> orderItems;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
	public List<String> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	public Map<Long, OrderItemStatus> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(Map<Long, OrderItemStatus> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}
