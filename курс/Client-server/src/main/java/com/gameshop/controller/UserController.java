package com.gameshop.controller;

import com.gameshop.dto.UserDTO;
import com.gameshop.model.User;
import com.gameshop.service.UserService;
import com.gameshop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO createdUser = userService.registerUser(userDTO);
            String token = jwtUtil.generateToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRole());
            Map<String, Object> response = new HashMap<>();
            response.put("user", createdUser);
            response.put("token", token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            User user = userService.login(email, password);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole().name());
            userDTO.setStatus(user.getStatus().name());
            userDTO.setLastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : null);
            userDTO.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
            Map<String, Object> response = new HashMap<>();
            response.put("user", userDTO);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}



