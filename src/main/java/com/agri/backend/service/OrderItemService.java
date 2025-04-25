package com.agri.backend.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.agri.backend.Exceptions.UDException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.CartItem;
import com.agri.backend.entity.OrderItem;
import com.agri.backend.entity.OrderItem.OrderItemStatus;
import com.agri.backend.entity.Product;
import com.agri.backend.entity.User;
import com.agri.backend.repository.OrderItemRepo;
import com.agri.backend.repository.ProductRepository;
import com.agri.backend.repository.UserRepo;

@Service
public class OrderItemService {
	@Autowired
	private OrderItemRepo orderItemRepo;
	@Autowired 
	private UserRepo userRepo;
	@Autowired 
	private CartService cartService;
	@Autowired
	private ProductRepository productRepo;
	
	public List<OrderItem> getOrderItems(){
		return orderItemRepo.findAll();
	}
	
	public OrderItem getOrderItem(long id) throws UDException {
		return orderItemRepo.findById(id).orElseThrow(() -> new UDException("Order Not found with id " + id , HttpStatus.NOT_FOUND));
	}
	
	public OrderItem createOrderItem(OrderItem item) throws UDException {
		User user = userRepo.findById(item.getUserId()).orElseThrow(() -> new UDException("User not found with Id" + item.getUserId(), HttpStatus.NOT_FOUND));
		User farmer = userRepo.findById(item.getFarmerId()).orElseThrow(() -> new UDException("Farmer not found with Id" + item.getUserId(), HttpStatus.NOT_FOUND));
		OrderItem orderItem = orderItemRepo.save(item);
		Map<Long,OrderItemStatus> orders = user.getOrderItems();
		orders.put(orderItem.getId(), orderItem.getStatus());
		user.setOrderItems(orders);
		orders = farmer.getOrderItems();
		orders.put(orderItem.getId(), orderItem.getStatus());
		farmer.setOrderItems(orders);
		userRepo.save(user);
		userRepo.save(farmer);
		return orderItem;
	}
	
	public void placeOrder(Cart cart) throws UDException {
		List<CartItem> items = cart.getItems();
		for(CartItem item:items) {
			OrderItem orderItem = new OrderItem();
			orderItem.setFarmerId(item.getFarmerId());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setProductId(item.getProductId());
			orderItem.setStatus(OrderItemStatus.PENDING);
			orderItem.setUserId(cart.getId());
			orderItem.setTotalPrice(item.getTotalPrice());
			orderItem = createOrderItem(orderItem);
		}
		cartService.deleteItems(cart);
	}

	public OrderItem updateOrderItem(long id, OrderItemStatus status) throws UDException {
		OrderItem order = orderItemRepo.findById(id).orElseThrow(() -> new UDException("Order Not found with id " + id , HttpStatus.NOT_FOUND));
		order.setStatus(status);
		Product product = productRepo.findById(order.getProductId()).orElseThrow(() -> new UDException("Product not found with id " + order.getProductId(), HttpStatus.NOT_FOUND));
		product.setAvailableQuantity(product.getAvailableQuantity() - order.getQuantity());
		User user = userRepo.findById(order.getUserId()).orElseThrow(() -> new UDException("User not found with Id" + order.getUserId(), HttpStatus.NOT_FOUND));
		User farmer = userRepo.findById(order.getFarmerId()).orElseThrow(() -> new UDException("Farmer not found with Id" + order.getUserId(), HttpStatus.NOT_FOUND));
		Map<Long,OrderItemStatus> orders = user.getOrderItems();
		orders.put(order.getId(), status);
		user.setOrderItems(orders);
		orders = farmer.getOrderItems();
		orders.put(order.getId(), status);
		farmer.setOrderItems(orders);
		userRepo.save(user);
		userRepo.save(farmer);
		return orderItemRepo.save(order);
	}
}
