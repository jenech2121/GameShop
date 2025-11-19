package com.gameshop.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private Long userId;
    /**
     * ACCEPTED values: DELIVERED, CANCELLED
     */
    private String action;
}





