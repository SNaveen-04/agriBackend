package com.agri.backend.controller;


import com.agri.backend.Exceptions.ProductException;
import com.agri.backend.entity.Product;
import com.agri.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<?> getProductsByUserId(@PathVariable Long userId) {
        try {
            List<Product> products = productService.getProductsByUserId(userId);
            return ResponseEntity.ok(products);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            String response = productService.deleteProduct(id);
            return ResponseEntity.ok(response);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    
    @GetMapping("category/{categoryId}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable Long categoryId) {
        try {
            List<Product> products = productService.getProductsByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    
    @GetMapping("offers")
    public ResponseEntity<?> getProductOffers() {
        try {
            List<Product> products = productService.getProductOffers();
            return ResponseEntity.ok(products);
        } catch (ProductException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
