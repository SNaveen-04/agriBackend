package com.agri.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.agri.backend.Exceptions.ProductException;
import com.agri.backend.entity.Category;
import com.agri.backend.entity.Product;
import com.agri.backend.entity.User;
import com.agri.backend.repository.CategoryRepo;
import com.agri.backend.repository.ProductRepository;
import com.agri.backend.repository.UserRepo;

import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CategoryRepo categoryRepo;

	public Product saveProduct(Product product) {
		User user = userRepo.findById(product.getUserId()).orElseThrow(
				() -> new ProductException("User not found with id: " + product.getUserId(), HttpStatus.NOT_FOUND));
		Category category = categoryRepo.findById(product.getCategoryId())
				.orElseThrow(() -> new ProductException("Category not found with id: " + product.getCategoryId(),
						HttpStatus.NOT_FOUND));
		product.setUserId(user.getId());
		product.setUserName(user.getUserName());
		product.setCategory(category.getName());
		float discount = product.getDiscount();
		double price = product.getPrice();
		float offerPrice = (float) (price - (price * discount / 100));
		product.setOfferPrice(offerPrice);
		return productRepository.save(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<Product> getProductOffers() {
		return productRepository.findByDiscountGreaterThan(0);
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductException("Product not found with ID: " + id, HttpStatus.NOT_FOUND));
	}

	public List<Product> getProductsByUserId(Long userId) {
		List<Product> products = productRepository.findByUserId(userId);
		if (products.isEmpty()) {
			throw new ProductException("No products found for User ID: " + userId, HttpStatus.NOT_FOUND);
		}
		return products;
	}

	public String deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ProductException("Product not found with ID: " + id, HttpStatus.NOT_FOUND);
		}
		productRepository.deleteById(id);
		return "Product deleted successfully";
	}

	public List<Product> getProductsByCategoryId(Long categoryId) {
		List<Product> products = productRepository.findByCategoryId(categoryId);
		if (products.isEmpty()) {
			throw new ProductException("No products found for category ID: " + categoryId, HttpStatus.NOT_FOUND);
		}
		return products;
	}
}
