package com.agri.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaction_Table")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  // Associated order

    private String paymentMethod;  // e.g., Credit Card, UPI, Cash on Delivery

    private double amount;  // Total transaction amount

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;  // SUCCESS, FAILED, PENDING

    private LocalDateTime transactionTimestamp;  // Date and time of transaction

    public enum PaymentStatus {
        SUCCESS, FAILED, PENDING
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public LocalDateTime getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public void setTransactionTimestamp(LocalDateTime transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}
    
}
