package org.example.blogapplication.services;

import jakarta.transaction.Transactional;
import org.example.blogapplication.models.Role;
import org.example.blogapplication.models.User;
import org.example.blogapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Add this
    @Autowired
    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create a new user (with validation)
    public User createUser(User user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User fields cannot be null");
        }
        // ✅ Assign default role if none provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }
        // 👉 Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Update existing user (more robust)
    public User updateUser(Long userId, User updatedUser) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if (updatedUser.getUsername() != null) {
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if (updatedUser.getEmail() != null) {
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    if (updatedUser.getPassword() != null) {
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // encode!
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    // Get single user
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public void deleteUser(Long id) {
        // Solution 1 (simple) :
        userRepository.deleteById(id);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Additional useful method
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }
    /**
     * Find user by username with proper error handling
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll((org.springframework.data.domain.Pageable) pageable);
    }
}