package com.gameshop.service;

import com.gameshop.dto.UserDTO;
import com.gameshop.model.User;
import com.gameshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO);
    }
    
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("User with email " + userDTO.getEmail() + " already exists");
        }
        
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFullName(resolveFullName(userDTO));
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(User.UserRole.USER);
        user.setStatus(User.UserStatus.ACTIVE);
        
        user = userRepository.save(user);
        return toDTO(user);
    }
    
    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        if (user.getStatus() == User.UserStatus.BLOCKED) {
            throw new RuntimeException("User account is blocked");
        }

        user.markLogin();
        userRepository.save(user);
        return user;
    }
    
    @Transactional
    public UserDTO updateUserStatus(Long id, User.UserStatus status, Long currentUserId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Проверка: администратор не может изменять свой собственный статус
        if (currentUserId != null && user.getId().equals(currentUserId)) {
            throw new RuntimeException("You cannot change your own status");
        }
        
        user.setStatus(status);
        user = userRepository.save(user);
        return toDTO(user);
    }
    
    @Transactional
    public UserDTO updateUserRole(Long id, User.UserRole role, Long currentUserId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Проверка: администратор не может изменять свою собственную роль
        if (currentUserId != null && user.getId().equals(currentUserId)) {
            throw new RuntimeException("You cannot change your own role");
        }
        
        user.setRole(role);
        user = userRepository.save(user);
        return toDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getActiveUsers() {
        return userRepository.findByStatus(User.UserStatus.ACTIVE).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.getStatus().name());
        dto.setLastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : null);
        dto.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        return dto;
    }

    private String resolveFullName(UserDTO userDTO) {
        String provided = userDTO.getFullName();
        if (provided != null && !provided.trim().isEmpty()) {
            return provided.trim();
        }
        String email = userDTO.getEmail();
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf('@'));
        }
        return "User";
    }
}



