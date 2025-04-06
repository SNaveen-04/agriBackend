package com.agri.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Product_Table")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;  

    private long userId;
    private String name;
    private double price;
    private float discount;
    private float offerPrice;
    private String priceType;
    private int noOfReviews;
    private float rating;
    private float availableQuantity;
    private String img;
    private String description;
    private String category;
    private long categoryId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(float offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public int getNoOfReviews() {
		return noOfReviews;
	}

	public void setNoOfReviews(int noOfReviews) {
		this.noOfReviews = noOfReviews;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public float getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(float availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
    
	
    
}
