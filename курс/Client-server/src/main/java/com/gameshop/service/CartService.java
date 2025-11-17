package com.gameshop.service;

import com.gameshop.dto.CartItemDTO;
import com.gameshop.dto.CartSyncItem;
import com.gameshop.dto.CartSyncRequest;
import com.gameshop.dto.ProductDTO;
import com.gameshop.model.CartItem;
import com.gameshop.model.Product;
import com.gameshop.model.User;
import com.gameshop.repository.CartItemRepository;
import com.gameshop.repository.ProductRepository;
import com.gameshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return cartItemRepository.findByUser(user).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CartItemDTO addToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        CartItem existingItem = cartItemRepository.findByUserAndProductId(user, productId)
                .orElse(null);
        
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem = cartItemRepository.save(existingItem);
            return toDTO(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem = cartItemRepository.save(cartItem);
            return toDTO(cartItem);
        }
    }
    
    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));
        
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("Cart item does not belong to user");
        }
        
        cartItemRepository.delete(cartItem);
    }
    
    @Transactional
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        cartItemRepository.deleteByUser(user);
    }

    @Transactional
    public List<CartItemDTO> syncCart(CartSyncRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("userId is required");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        cartItemRepository.deleteByUser(user);

        if (request.getItems() != null) {
            for (CartSyncItem item : request.getItems()) {
                if (item.getProductId() == null) continue;
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProductId()));
                CartItem cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(item.getQuantity() != null ? item.getQuantity() : 1);
                cartItemRepository.save(cartItem);
            }
        }

        return cartItemRepository.findByUser(user).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private CartItemDTO toDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setUserId(cartItem.getUser().getId());
        dto.setProduct(ProductDTO.fromEntity(cartItem.getProduct()));
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}




