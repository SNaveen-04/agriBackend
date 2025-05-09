package com.agri.backend.entity;

import java.util.List;
import java.util.Map;

import com.agri.backend.entity.OrderItem.OrderItemStatus;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "User_Table")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long Id;
	private String userName;
	private String email;
	private String number;
	private String password;
	@Column(nullable = false)
	private String userType;
	@ElementCollection
	private Map<Long,OrderItemStatus> orderItems;
	private List<String> addresses;
	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Map<Long, OrderItemStatus> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Map<Long, OrderItemStatus> orderItems) {
		this.orderItems = orderItems;
	}
	
	

}
