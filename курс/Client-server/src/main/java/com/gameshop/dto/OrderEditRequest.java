package com.gameshop.dto;

import lombok.Data;

@Data
public class OrderEditRequest extends OrderRequest {
    private Long userId;
}


