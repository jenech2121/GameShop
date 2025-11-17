package com.gameshop.service;

import com.gameshop.dto.OrderDTO;
import com.gameshop.dto.OrderEditRequest;
import com.gameshop.dto.OrderRequest;
import com.gameshop.dto.OrderStatusUpdateRequest;
import com.gameshop.model.*;
import com.gameshop.repository.CartItemRepository;
import com.gameshop.repository.OrderRepository;
import com.gameshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderDTO createOrder(Long userId, OrderRequest orderRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setDeliveryStatus(Order.DeliveryStatus.IN_PROGRESS);
        order.setExpectedDeliveryAt(LocalDateTime.now().plusHours(2));
        applyPaymentInfo(order, orderRequest);

        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return OrderDTO.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return orderRepository.findByUser(user).stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getActiveOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(order -> order.getDeliveryStatus() == Order.DeliveryStatus.IN_PROGRESS
                        || order.getDeliveryStatus() == Order.DeliveryStatus.AWAITING_CONFIRMATION)
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrder(Long userId, Long orderId, OrderEditRequest request) {
        Order order = validateUserOrder(userId, orderId);
        if (order.getDeliveryStatus() != Order.DeliveryStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot edit order that is already completed or cancelled");
        }
        applyPaymentInfo(order, request);
        order = orderRepository.save(order);
        return OrderDTO.fromEntity(order);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long requesterId, Long orderId, OrderStatusUpdateRequest request, boolean isAdmin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (!isAdmin && (requesterId == null || !order.getUser().getId().equals(requesterId))) {
            throw new RuntimeException("You are not allowed to update this order");
        }

        String action = request.getAction() != null ? request.getAction().toUpperCase() : "";
        switch (action) {
            case "DELIVERED":
                order.setStatus(Order.OrderStatus.DELIVERED);
                order.setDeliveryStatus(Order.DeliveryStatus.DELIVERED);
                break;
            case "CANCELLED":
                order.setStatus(Order.OrderStatus.CANCELLED);
                order.setDeliveryStatus(Order.DeliveryStatus.CANCELLED);
                break;
            default:
                throw new RuntimeException("Unknown action: " + request.getAction());
        }
        order = orderRepository.save(order);
        return OrderDTO.fromEntity(order);
    }

    @Transactional
    public OrderDTO cancelOrderByAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setDeliveryStatus(Order.DeliveryStatus.CANCELLED);
        order = orderRepository.save(order);
        return OrderDTO.fromEntity(order);
    }

    private Order validateUserOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order does not belong to user");
        }
        return order;
    }

    private void applyPaymentInfo(Order order, OrderRequest orderRequest) {
        if (orderRequest == null) {
            return;
        }
        order.setPaymentMethod(limitLength(orderRequest.getPaymentMethod(), 50));
        order.setPaymentType(limitLength(orderRequest.getPaymentType(), 50));
        order.setDeliveryAddress(limitLength(orderRequest.getDeliveryAddress(), 255));
        order.setCardNumber(limitDigits(orderRequest.getCardNumber(), 12));
        order.setCardExpiry(limitLength(orderRequest.getCardExpiry(), 5));
        order.setCardCvv(limitDigits(orderRequest.getCardCvv(), 3));
    }

    private String limitLength(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }
        return trimmed.substring(0, maxLength);
    }

    private String limitDigits(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (digitsOnly.isEmpty()) {
            return null;
        }
        if (digitsOnly.length() <= maxLength) {
            return digitsOnly;
        }
        return digitsOnly.substring(0, maxLength);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateAwaitingConfirmationOrders() {
        List<Order> overdue = orderRepository.findByDeliveryStatusAndExpectedDeliveryAtBefore(
                Order.DeliveryStatus.IN_PROGRESS, LocalDateTime.now());
        for (Order order : overdue) {
            order.setDeliveryStatus(Order.DeliveryStatus.AWAITING_CONFIRMATION);
            orderRepository.save(order);
        }
    }
}
