package com.agri.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.agri.backend.DTO.OrderDTO;
import com.agri.backend.entity.Order.OrderStatus;
import com.agri.backend.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public OrderDTO placeOrder(@RequestParam Long userId, @RequestParam Long cartId) {
        return orderService.placeOrder(userId, cartId);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
        return "Order status updated successfully";
    }
}
