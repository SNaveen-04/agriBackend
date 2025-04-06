package com.agri.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.agri.backend.DTO.UserDTO;
import com.agri.backend.entity.User;
import com.agri.backend.Exceptions.UserException;
import com.agri.backend.service.UserService;

import lombok.Data;

import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("login")
    public ResponseEntity<?> UserLogin(@RequestBody LoginUser user){
    	try {
    		UserDTO loggedUser = userService.verifyUser(user.getEmail(), user.getPassword());
    		return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
    	}
    	catch(UserException e) {
    		return ResponseEntity.status(e.getStatus()).body(e.getMessage());    		
    	}
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            UserDTO createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

//    @GetMapping("{id}")
//    public ResponseEntity<?> getUserById(@PathVariable Long id) {
//        try {
//            UserDTO user = userService.getUserById(id);
//            return ResponseEntity.ok(user);
//        } catch (UserException e) {
//            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//        }
//    }

//    @GetMapping("email/{email}")
//    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
//        try {
//            UserDTO user = userService.getUserByEmail(email);
//            return ResponseEntity.ok(user);
//        } catch (UserException e) {
//            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("phone/{number}")
//    public ResponseEntity<?> getUserByPhone(@PathVariable String number) {
//        try {
//            UserDTO user = userService.getUserByPhone(number);
//            return ResponseEntity.ok(user);
//        } catch (UserException e) {
//            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//        }
//    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUser) {
        try {
            UserDTO user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody String updatedPassword) {
        try {
            UserDTO user = userService.updatePassword(id, updatedPassword);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            String response = userService.deleteUser(id);
            return ResponseEntity.ok(response);
        } catch (UserException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}


@Data
class LoginUser{
	private String email;
	private String password;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
