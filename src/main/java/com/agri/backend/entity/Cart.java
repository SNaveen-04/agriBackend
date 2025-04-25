package com.agri.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "Cart_Table")
@Data
public class Cart {

    @Id
    private long id;

    private double originalPrice;
    private double discountedPrice;
    private double finalTotalPrice;
    @ElementCollection
    private List<CartItem> items = new ArrayList<CartItem>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public double getFinalTotalPrice() {
        return finalTotalPrice;
    }

    public void setFinalTotalPrice(double finalTotalPrice) {
        this.finalTotalPrice = finalTotalPrice;
    }

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	
	public void addItem(CartItem item) {
		this.items.add(item);
	}

	public void removeItem(long id) {
		this.items = this.items.stream().filter((item) -> item.getId() != id).collect(Collectors.toList());
	}
	
	public void updateItem(CartItem item) {
		this.items = this.items.stream().map((temp) -> {
			if(item.getId() == temp.getId()) {
				temp.setQuantity(item.getQuantity());
				temp.setTotalPrice(item.getTotalPrice());
			}
			return temp;
		}).collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return "Cart [id=" + id + ", originalPrice=" + originalPrice + ", discountedPrice=" + discountedPrice
				+ ", finalTotalPrice=" + finalTotalPrice + ", items=" + items + "]";
	}

	
}
