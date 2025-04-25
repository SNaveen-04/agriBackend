package com.agri.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agri.backend.Exceptions.UDException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.OrderItem;
import com.agri.backend.entity.OrderItem.OrderItemStatus;
import com.agri.backend.service.CartService;
import com.agri.backend.service.OrderItemService;

@RestController
@RequestMapping("order")
public class OrderItemController {
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private CartService cartService;
	
	@GetMapping("")
	public List<OrderItem> getAllOrders() {
		return orderItemService.getOrderItems();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderItem(@PathVariable long id){
		try {
			return new ResponseEntity<>(orderItemService.getOrderItem(id),HttpStatus.OK);			
		}catch(UDException e) {
			return new ResponseEntity<>(e.getMessage(),e.getStatus());
		}
	}
	
	@PutMapping("")
	public ResponseEntity<?> updateOrderItem(@RequestParam long id, @RequestParam OrderItemStatus status){
		try {
			return new ResponseEntity<>(orderItemService.updateOrderItem(id,status),HttpStatus.OK);			
		}catch(UDException e) {
			return new ResponseEntity<>(e.getMessage(),e.getStatus());
		}
	} 
	
	@PostMapping("")
	public ResponseEntity<?> createOrderItem(@RequestBody OrderItem item){
		try {
			OrderItem newItem = orderItemService.createOrderItem(item);
			return new ResponseEntity<>(newItem,HttpStatus.OK);
		} catch (UDException e) {
			return new ResponseEntity<>(e.getMessage(),e.getStatus());
		}
	}
	
	@PostMapping("place")
	public ResponseEntity<?> placeOrder(@RequestParam long cartId){
		try {
			Cart cart = cartService.getCart(cartId);
			orderItemService.placeOrder(cart);
			return new ResponseEntity<>("Success",HttpStatus.OK);
		} catch (UDException e) {
			return new ResponseEntity<>(e.getMessage(),e.getStatus());
		}
	}
}
