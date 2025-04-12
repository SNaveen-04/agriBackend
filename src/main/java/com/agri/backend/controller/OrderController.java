package com.agri.backend.controller;

import com.agri.backend.DTO.OrderDTO;
import com.agri.backend.config.RazorpayConfig;
import com.agri.backend.entity.Order.OrderStatus;
import com.agri.backend.payload.CreateOrderRequest;
import com.agri.backend.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private RazorpayConfig razorpayProperties;

    @Autowired
    private OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            RazorpayClient razorpay = new RazorpayClient(
                razorpayProperties.getKeyId(),
                razorpayProperties.getKeySecret()
            );

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount() * 100); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("payment_capture", 1);

            Order order = razorpay.orders.create(orderRequest);
            RazorpayOrderResponse response = new RazorpayOrderResponse(
                order.get("id"),
                order.get("amount"),
                order.get("currency")
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/place")
    public ResponseEntity<OrderDTO> placeOrder(@RequestParam Long userId, @RequestParam Long cartId) {
        return ResponseEntity.ok(orderService.placeOrder(userId, cartId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<OrderDTO>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("Order status updated successfully");
    }
}


class RazorpayOrderResponse {
    private String id;
    private int amount;
    private String currency;
    
    
	public RazorpayOrderResponse(String id, int amount, String currency) {
		super();
		this.id = id;
		this.amount = amount;
		this.currency = currency;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
