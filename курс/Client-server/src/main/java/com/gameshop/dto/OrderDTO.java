package com.gameshop.dto;

import com.gameshop.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private Double totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expectedDeliveryAt;
    private String deliveryStatus;
    private String paymentMethod;
    private String paymentType;
    private String deliveryAddress;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;
    private List<OrderItemDTO> items;

    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        dto.setUserEmail(order.getUser() != null ? order.getUser().getEmail() : null);
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        dto.setCreatedAt(order.getCreatedAt());
        dto.setExpectedDeliveryAt(order.getExpectedDeliveryAt());
        dto.setDeliveryStatus(order.getDeliveryStatus() != null ? order.getDeliveryStatus().name() : null);
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentType(order.getPaymentType());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setCardNumber(maskSensitive(order.getCardNumber(), 4));
        dto.setCardExpiry(order.getCardExpiry());
        dto.setCardCvv(maskSensitive(order.getCardCvv(), 0));
        dto.setItems(order.getOrderItems() == null ? Collections.emptyList() :
                order.getOrderItems().stream()
                        .map(OrderItemDTO::fromEntity)
                        .collect(Collectors.toList()));
        return dto;
    }

    private static String maskSensitive(String value, int visibleChars) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String trimmed = value.trim();
        if (visibleChars <= 0) {
            return trimmed.replaceAll(".", "*");
        }
        int length = trimmed.length();
        if (length <= visibleChars) {
            return trimmed;
        }
        String masked = "*".repeat(Math.max(0, length - visibleChars));
        return masked + trimmed.substring(length - visibleChars);
    }
}

