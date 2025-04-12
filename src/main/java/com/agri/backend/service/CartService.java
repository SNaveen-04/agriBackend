package com.agri.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.agri.backend.Exceptions.ProductException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.CartItem;
import com.agri.backend.entity.Product;
import com.agri.backend.repository.CartItemRepo;
import com.agri.backend.repository.CartRepo;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    public Cart getCartByUserId(long userId) {
        Cart cart = cartRepo.findByUserId(userId);
        if (cart == null) {
            cart = createCart(userId);
        } else {
            recalculateCart(cart);
        }
        return cart;
    }

    public Cart createCart(long userId) {
        Cart existingCart = cartRepo.findByUserId(userId);
        if (existingCart != null) {
            throw new ProductException("Cart already exists for this user", HttpStatus.BAD_REQUEST);
        }
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setOriginalPrice(0);
        newCart.setDiscountedPrice(0);
        newCart.setFinalTotalPrice(0);
        return cartRepo.save(newCart);
    }

    public Cart addCartItemToCart(long cartId, CartItem newCartItem) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == newCartItem.getProduct().getId())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int updatedQuantity = existingItem.getQuantity() + newCartItem.getQuantity();
            existingItem.setQuantity(updatedQuantity);
        } else {
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
        }

        recalculateCart(cart);
        return cartRepo.save(cart);
    }

    public Cart updateCartItem(long cartId, long cartItemId, int newQuantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new ProductException("CartItem not found", HttpStatus.NOT_FOUND));

        cartItem.setQuantity(newQuantity);
        recalculateCart(cart);
        return cartRepo.save(cart);
    }

    public Cart deleteItem(long cartId, long cartItemId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));

        CartItem cartItemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new ProductException("CartItem not found", HttpStatus.NOT_FOUND));

        cart.getCartItems().remove(cartItemToRemove);
        cartItemRepo.deleteById(cartItemId);
        recalculateCart(cart);
        return cartRepo.save(cart);
    }

    private void recalculateCart(Cart cart) {
        double originalPrice = 0.0;
        double finalTotal = 0.0;

        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            double price = product.getPrice();
            double discount = price * (product.getDiscount() / 100);
            double discountedPrice = price - discount;
            int quantity = item.getQuantity();

            double itemOriginal = price * quantity;
            double itemTotal = discountedPrice * quantity;

            item.setTotalPrice(itemTotal);

            originalPrice += itemOriginal;
            finalTotal += itemTotal;
        }

        double totalDiscount = originalPrice - finalTotal;

        cart.setOriginalPrice(originalPrice);
        cart.setDiscountedPrice(totalDiscount);
        cart.setFinalTotalPrice(finalTotal);
    }
}
