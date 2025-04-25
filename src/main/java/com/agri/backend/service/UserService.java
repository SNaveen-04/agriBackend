package com.agri.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.agri.backend.DTO.UserDTO;
import com.agri.backend.DTO.DTOMapper;
import com.agri.backend.entity.OrderItem.OrderItemStatus;
import com.agri.backend.entity.User;
import com.agri.backend.Exceptions.UserException;
import com.agri.backend.repository.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public UserDTO getUserById(long id) throws UserException {
		User savedUser = userRepo.findById(id)
				.orElseThrow(() -> new UserException("User not found with id " + id, HttpStatus.NOT_FOUND));
		return DTOMapper.convertUserToUserDTO(savedUser);
	}

	public UserDTO createUser(User user) throws UserException {
		String email = user.getEmail();
		String number = user.getNumber();

		if (userRepo.findByEmail(email).isPresent()) {
			throw new UserException("User already exists with email: " + email, HttpStatus.OK);
		}
		if (userRepo.findByNumber(number).isPresent()) {
			throw new UserException("User already exists with phone number: " + number, HttpStatus.OK);
		}

		try {
			User savedUser = userRepo.save(user);
			return DTOMapper.convertUserToUserDTO(savedUser);
		} catch (Exception e) {
			throw new UserException("Something went wrong while creating the user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public UserDTO getUserById(Long id) throws UserException {
		User existingUser = userRepo.findById(id)
				.orElseThrow(() -> new UserException("User not found with ID: " + id, HttpStatus.NOT_FOUND));
		return DTOMapper.convertUserToUserDTO(existingUser);
	}

	public UserDTO getUserByEmail(String email) throws UserException {
		User existingUser = userRepo.findByEmail(email)
				.orElseThrow(() -> new UserException("User not found with email: " + email, HttpStatus.NOT_FOUND));
		return DTOMapper.convertUserToUserDTO(existingUser);
	}

	public UserDTO getUserByPhone(String number) throws UserException {
		User existingUser = userRepo.findByNumber(number).orElseThrow(
				() -> new UserException("User not found with phone number: " + number, HttpStatus.NOT_FOUND));
		return DTOMapper.convertUserToUserDTO(existingUser);
	}

	public UserDTO updateUser(Long id, UserDTO updatedUser) throws UserException {
		User existingUser = userRepo.findById(id)
				.orElseThrow(() -> new UserException("User not found with ID: " + id, HttpStatus.NOT_FOUND));

		if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
			if (userRepo.findByEmail(updatedUser.getEmail()).isPresent()) {
				throw new UserException("Email is already in use: " + updatedUser.getEmail(), HttpStatus.CONFLICT);
			}
			existingUser.setEmail(updatedUser.getEmail());
		}

		if (updatedUser.getNumber() != null && !updatedUser.getNumber().equals(existingUser.getNumber())) {
			if (userRepo.findByNumber(updatedUser.getNumber()).isPresent()) {
				throw new UserException("Phone number is already in use: " + updatedUser.getNumber(),
						HttpStatus.CONFLICT);
			}
			existingUser.setNumber(updatedUser.getNumber());
		}

		if (updatedUser.getUserName() != null) {
			existingUser.setUserName(updatedUser.getUserName());
		}
		existingUser.setOrderItems(updatedUser.getOrderItems());
		User savedUser = userRepo.save(existingUser);
		return DTOMapper.convertUserToUserDTO(savedUser);
	}

	public String deleteUser(Long id) throws UserException {
		Optional<User> userOptional = userRepo.findById(id);

		if (!userOptional.isPresent()) {
			throw new UserException("No user exists with ID: " + id, HttpStatus.NOT_FOUND);
		}

		try {
			userRepo.delete(userOptional.get());
			return "User deleted successfully";
		} catch (Exception e) {
			throw new UserException("Something went wrong while deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public UserDTO updatePassword(Long id, String updatedPassword) throws UserException {
		User existingUser = userRepo.findById(id)
				.orElseThrow(() -> new UserException("User not found with ID: " + id, HttpStatus.NOT_FOUND));

		if (updatedPassword.equals(existingUser.getPassword())) {
			throw new UserException("Old and New Passwords are the same", HttpStatus.CONFLICT);
		}

		existingUser.setPassword(updatedPassword);
		User savedUser = userRepo.save(existingUser);
		return DTOMapper.convertUserToUserDTO(savedUser);
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepo.findAll();
		return users.stream().map(DTOMapper::convertUserToUserDTO).collect(Collectors.toList());
	}

	public UserDTO verifyUser(String userEmail, String password, String userType) throws UserException {
		User existingUser = userRepo.findByEmail(userEmail)
				.orElseThrow(() -> new UserException("User not found with email: " + userEmail, HttpStatus.NOT_FOUND));
		if (existingUser.getPassword().equals(password) && existingUser.getUserType().equals(userType)) {
			return DTOMapper.convertUserToUserDTO(existingUser);
		}
		throw new UserException("Invalid Credentials", HttpStatus.NOT_FOUND);
	}

	public OrderList getOrders(Long userId) throws UserException {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User not found with ID: " + userId, HttpStatus.NOT_FOUND));
		OrderList orderList = new OrderList();
		orderList.setCompleted(user.getOrderItems().keySet().stream()
				.filter(k -> user.getOrderItems().get(k) == OrderItemStatus.DELIVERED).toList());
		orderList.setPending(user.getOrderItems().keySet().stream()
				.filter(k -> user.getOrderItems().get(k) == OrderItemStatus.PENDING).toList());
		orderList.setCancelled(user.getOrderItems().keySet().stream()
				.filter(k -> user.getOrderItems().get(k) == OrderItemStatus.CANCELLED).toList());
		orderList.setTotalOrders(user.getOrderItems().keySet().stream().map(k -> k).toList());
		return orderList;
	}
}

class OrderList {
	private List<Long> completed;
	private List<Long> pending;
	private List<Long> cancelled;
	private List<Long> totalOrders;

	public List<Long> getCompleted() {
		return completed;
	}

	public void setCompleted(List<Long> completed) {
		this.completed = completed;
	}

	public List<Long> getPending() {
		return pending;
	}

	public void setPending(List<Long> pending) {
		this.pending = pending;
	}

	public List<Long> getCancelled() {
		return cancelled;
	}

	public void setCancelled(List<Long> cancelled) {
		this.cancelled = cancelled;
	}

	public List<Long> getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(List<Long> totalOrders) {
		this.totalOrders = totalOrders;
	}

}
