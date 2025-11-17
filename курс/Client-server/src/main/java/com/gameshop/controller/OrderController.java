package com.gameshop.controller;

import com.gameshop.dto.OrderDTO;
import com.gameshop.dto.OrderEditRequest;
import com.gameshop.dto.OrderRequest;
import com.gameshop.dto.OrderStatusUpdateRequest;
import com.gameshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class OrderController {
    private final OrderService orderService;
    
    @PostMapping("/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId, @RequestBody(required = false) OrderRequest orderRequest) {
        try {
            OrderDTO order = orderService.createOrder(userId, orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(orderService.getUserOrders(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> editOrder(@PathVariable Long orderId, @RequestBody OrderEditRequest request) {
        try {
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
            }
            return ResponseEntity.ok(orderService.updateOrder(request.getUserId(), orderId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateRequest request) {
        try {
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
            }
            return ResponseEntity.ok(orderService.updateOrderStatus(request.getUserId(), orderId, request, false));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}




