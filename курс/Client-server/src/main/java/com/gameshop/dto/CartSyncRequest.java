package com.gameshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartSyncRequest {
    private Long userId;
    private List<CartSyncItem> items;
}





