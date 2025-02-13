package com.nt.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.demo.User;
import com.nt.demo.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserRepository userRepository;
	public UserController(UserRepository userRepository)
	{
		this.userRepository=userRepository;
	}
	@GetMapping
	public List<User> getAllUsers()
	{
		return userRepository.findAll();
	}
	public ResponseEntity<User> getUserById(@PathVariable Long id)
	{
		Optional<User>user= userRepository.findById(id);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	 // Create new user
    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
            .map(user -> {
                user.setName(userDetails.getName());
                user.setEmail(userDetails.getEmail());
                user.setPassword(userDetails.getPassword());
                return ResponseEntity.ok(userRepository.save(user));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> {
                userRepository.delete(user);
                return ResponseEntity.ok("User deleted successfully");
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


