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
    public Cart addCartItemToCart(long cartId, CartItem newCartItem) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));

        // Check if the product already exists in the cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == newCartItem.getProduct().getId())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity and total price if product already exists
            int updatedQuantity = existingItem.getQuantity() + newCartItem.getQuantity();
            existingItem.setQuantity(updatedQuantity);
            existingItem.setTotalPrice(existingItem.getProduct().getPrice() * updatedQuantity);
        } else {
            // Add as new item
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
        }

        // Recalculate total cart price
        cart.setTotalPrice(cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());

        cartRepo.save(cart);
        return cart;
    }


    // Update CartItem (e.g., update quantity and recalculate total price)
    public Cart updateCartItem(long cartId, long cartItemId, int newQuantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new ProductException("CartItem not found", HttpStatus.NOT_FOUND));

        Product product = cartItem.getProduct();
        double originalPrice = product.getPrice();
        float discount = product.getDiscount();

        // Calculate discounted price
        double discountedPrice = originalPrice - (originalPrice * discount / 100);

        // Update quantity and total price
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(discountedPrice * newQuantity);

        // Recalculate total cart price
        double updatedCartTotal = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(updatedCartTotal);

        cartRepo.save(cart);
        return cart;
    }

    
    public Cart deleteItem(long cartId, long cartItemId) {
        // Fetch the cart by ID or throw exception if not found
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ProductException("Cart not found", HttpStatus.NOT_FOUND));

        // Find the cart item to be deleted or throw exception if not found
        CartItem cartItemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new ProductException("CartItem not found", HttpStatus.NOT_FOUND));

        // Remove the item from the cart and delete it from DB
        cart.getCartItems().remove(cartItemToRemove);
        cartItemRepo.deleteById(cartItemId);

        // Recalculate total cart price considering discounts
        double updatedTotal = 0.0;
        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            float discount = product.getDiscount();
            double unitPrice = product.getPrice();
            double discountedPrice = unitPrice - (unitPrice * discount / 100);
            double itemTotal = discountedPrice * item.getQuantity();
            item.setTotalPrice(itemTotal); // update the cart item price
            updatedTotal += itemTotal;
        }

        cart.setTotalPrice(updatedTotal);
        cartRepo.save(cart);

        return cart;
    }


    
}
