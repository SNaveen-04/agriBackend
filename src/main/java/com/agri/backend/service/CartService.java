package com.agri.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.agri.backend.Exceptions.UDException;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.CartItem;
import com.agri.backend.entity.Product;
import com.agri.backend.repository.CartItemRepo;
import com.agri.backend.repository.CartRepo;
import com.agri.backend.repository.ProductRepository;


@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;
    
    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private CartItemRepo cartItemRepo;
    
    public Cart getCartByid(long cartId) {
        Optional<Cart> cart = cartRepo.findById(cartId);
        if (cart.isPresent()) {
            recalculateCart(cart.get());
            return cart.get();
        } else {
            return createCart(cartId);
        }
    }


    public Cart getCart(long cartId) throws UDException {
    	return cartRepo.findById(cartId).orElseThrow(() -> new UDException("Cart Not Found",HttpStatus.NOT_FOUND));
    }
    
    public Cart createCart(long userId) {
        Cart newCart = new Cart();
        newCart.setId(userId);
        newCart.setOriginalPrice(0);
        newCart.setDiscountedPrice(0);
        newCart.setFinalTotalPrice(0);
        return cartRepo.save(newCart);
    }

    public Cart updateCart(Cart cart) {
    	recalculateCart(cart);
    	return cartRepo.save(cart);
    }
    
    public Cart deleteItems(Cart cart) throws UDException {
    	List<CartItem> items = cart.getItems();
    	cart.getItems().clear();
    	for(CartItem item:items) {
    		deleteCartItem(item.getId());
    	}
    	return updateCart(cart);
    }
    
    public void recalculateCart(Cart cart) {
        double original = 0, discounted = 0, finalTotal = 0;

        for (var item : cart.getItems()) {
        	Product product = productRepo.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("Product Not Found with Id" + item.getProductId()));
            original += item.getQuantity() * product.getPrice();
            discounted += item.getQuantity() * product.getDiscount();
        }

        finalTotal = original - discounted;
        cart.setOriginalPrice(original);
        cart.setDiscountedPrice(discounted);
        cart.setFinalTotalPrice(finalTotal);

        cartRepo.save(cart);
    }
    
    public CartItem saveItem(CartItem item) throws UDException {
    	Product product = productRepo.findById(item.getProductId()).orElseThrow(() -> new UDException("Product Not Found with Id " + item.getProductId(), HttpStatus.NOT_FOUND));
    	item.setFarmerId(product.getUserId());
    	item.setTotalPrice(item.getQuantity() * product.getPrice());
    	cartItemRepo.save(item);
    	return item;
    }
    
    public void deleteCartItem(long itemId) throws UDException {
    	cartItemRepo.findById(itemId).orElseThrow(() -> new UDException("No such Cart Item exists",HttpStatus.NOT_FOUND));
    	cartItemRepo.deleteById(itemId);
    }
    
    public CartItem updateCartItem(long itemId,int quantity) throws UDException {
    	CartItem item = cartItemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("No such Cart Item exists"));
    	item.setQuantity(quantity + item.getQuantity());
    	return recalculateItemPrice(item);
    }
    
    public CartItem recalculateItemPrice(CartItem item) throws UDException {
    	return saveItem(item);
    }
}
