package com.agri.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.agri.backend.Exceptions.ProductException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.CartItem;
import com.agri.backend.service.CartService;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable long cartId, @RequestParam long itemId) {
        try {
            Cart cart = cartService.deleteItem(cartId, itemId);
            return ResponseEntity.ok(cart);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createCart(@PathVariable long userId) {
        try {
            Cart createdCart = cartService.createCart(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{cartId}/add-item")
    public ResponseEntity<?> addCartItemToCart(@PathVariable long cartId, @RequestBody CartItem cartItem) {
        try {
            Cart updatedCart = cartService.addCartItemToCart(cartId, cartItem);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{cartId}/update-item/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable long cartId, @PathVariable long cartItemId, @RequestParam int quantity) {
        try {
            Cart updatedCart = cartService.updateCartItem(cartId, cartItemId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
