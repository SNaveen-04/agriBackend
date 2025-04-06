package com.agri.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Order_Table")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Customer who placed the order

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // List of ordered products

    private LocalDateTime orderTimestamp;  // Order date and time

    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // Pending, Shipped, Delivered, etc.

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Transaction transaction;  // Associated transaction details

    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public LocalDateTime getOrderTimestamp() {
		return orderTimestamp;
	}

	public void setOrderTimestamp(LocalDateTime orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
    
    
}
