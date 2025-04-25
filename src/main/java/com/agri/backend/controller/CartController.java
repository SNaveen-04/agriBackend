package com.agri.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agri.backend.Exceptions.UDException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.CartItem;
import com.agri.backend.payload.CartItemRequest;
import com.agri.backend.service.CartService;

@RestController
@RequestMapping("cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping("")
	public ResponseEntity<?> getCart(@RequestParam Long cartId) {
		try {
			Cart cart = cartService.getCartByid(cartId);
			System.out.println(cart);
			return ResponseEntity.ok(cart);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("item")
	public ResponseEntity<?> addItem(@RequestBody CartItemRequest itemReq, @RequestParam Long cartId) {
		try {
			Cart cart;
			cart = cartService.getCart(cartId);
			List<CartItem> items = cart.getItems().stream().filter((temp) -> temp.getProductId() == itemReq.getProductId()).toList();
			if(items.size() > 0) {
				CartItem item = cartService.updateCartItem(items.get(0).getId(), itemReq.getQuantity());
				cart.updateItem(item);
				cartService.updateCart(cart);
				return new ResponseEntity<>(cart,HttpStatus.OK);
			}
			CartItem item = new CartItem();
			item.setId(0);
			item.setProductId(itemReq.getProductId());
			item.setQuantity(itemReq.getQuantity());
			item.setCartId(cartId);
			item.setTotalPrice(0);
			CartItem savedItem;
			savedItem = cartService.saveItem(item);
			cart.addItem(savedItem);
			cartService.updateCart(cart);
			return new ResponseEntity<>(savedItem, HttpStatus.OK);
		} catch (UDException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		}
	}

	@DeleteMapping("item")
	public ResponseEntity<?> deleteItem(@RequestParam Long cartId, @RequestParam Long itemId) {
		Cart cart;
		try {
			cart = cartService.getCart(cartId);
			cart.removeItem(itemId);
			cartService.deleteCartItem(itemId);
			cartService.updateCart(cart);
			return new ResponseEntity<>(cart,HttpStatus.OK);
		} catch (UDException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		}
	}

	@PutMapping("item")
	public ResponseEntity<?> updateItem(@RequestParam Long itemId, @RequestParam Long cartId, @RequestParam int quantity) {
		Cart cart;
		try {
			cart = cartService.getCart(cartId);
			CartItem item = cartService.updateCartItem(itemId, quantity);
			cart.updateItem(item);
			cartService.updateCart(cart);
			return new ResponseEntity<>(cart,HttpStatus.OK);
		} catch (UDException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		}
	}
	
	@DeleteMapping("")
	public ResponseEntity<?> deleteItems(@RequestParam Long cartId) {
		Cart cart;
		try {
			cart = cartService.getCart(cartId);
			cartService.deleteItems(cart);
			return new ResponseEntity<>(cart,HttpStatus.OK);
		} catch (UDException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		}
	}

}
