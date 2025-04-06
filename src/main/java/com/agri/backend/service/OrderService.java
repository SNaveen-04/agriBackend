package com.agri.backend.service;

import com.agri.backend.DTO.OrderDTO;
import com.agri.backend.DTO.UserDTO;
import com.agri.backend.entity.Cart;
import com.agri.backend.entity.Order;
import com.agri.backend.entity.User;
import com.agri.backend.repository.CartRepo;
import com.agri.backend.repository.OrderRepo;
import com.agri.backend.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

	@Autowired
	private OrderRepo orderRepository;

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private CartRepo cartRepo;

	public OrderDTO placeOrder(Long userId, Long id) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isEmpty()) {
			throw new RuntimeException("User not found");
		}

		Cart cart = cartRepo.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
		Order order = new Order();
		order.setUser(userOptional.get());
		order.setCart(cart);
		order.setOrderTimestamp(LocalDateTime.now());
		order.setStatus(Order.OrderStatus.PENDING);

		order = orderRepository.save(order);

		return convertToDTO(order);
	}

	public List<OrderDTO> getAllOrders() {
		return orderRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Optional<OrderDTO> getOrderById(Long id) {
		return orderRepository.findById(id).map(this::convertToDTO);
	}

	public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			order.setStatus(status);
			orderRepository.save(order);
		} else {
			throw new RuntimeException("Order not found");
		}
	}

	private OrderDTO convertToDTO(Order order) {
		OrderDTO dto = new OrderDTO();
		dto.setId(order.getId());
		dto.setOrderTimestamp(order.getOrderTimestamp());
		dto.setStatus(order.getStatus().name());

		// Convert User -> UserDTO
		User user = order.getUser();
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUserName(user.getUserName());
		userDTO.setEmail(user.getEmail());
		userDTO.setNumber(user.getNumber());
		userDTO.setUserType(user.getUserType());
		dto.setCart(order.getCart());
		dto.setUser(userDTO);
		return dto;
	}
}
