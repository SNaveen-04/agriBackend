package com.agri.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.agri.backend.Exceptions.ProductException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.CartItem;
import com.agri.backend.repository.CartRepo;


@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    
    public Cart getCartByUserId(long userId) {
        Cart cart = cartRepo.findByUserId(userId);
        if (cart == null) {
            cart = createCart(userId);
        }
        return cart;
    }
    
    // Create Cart for a user
    public Cart createCart(long userId) {
        Cart existingCart = cartRepo.findByUserId(userId);
        if (existingCart != null) {
            throw new ProductException("Cart already exists for this user", HttpStatus.BAD_REQUEST);
        }
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setTotalPrice(0);
        return cartRepo.save(newCart);
    }

    // Add CartItem to an existing Cart
    public Cart addCartItemToCart(long cartId, CartItem cartItem) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getTotalPrice());
        cartRepo.save(cart);
        return cart;
    }

    // Update CartItem (e.g., update quantity and recalculate total price)
    public Cart updateCartItem(long cartId, long cartItemId, int newQuantity) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new ProductException("CartItem not found", HttpStatus.NOT_FOUND));
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(cartItem.getProduct().getPrice() * newQuantity);

        // Recalculate total cart price
        cart.setTotalPrice(cart.getCartItems().stream().mapToDouble(CartItem::getTotalPrice).sum());
        cartRepo.save(cart);
        return cart;
    }
}
