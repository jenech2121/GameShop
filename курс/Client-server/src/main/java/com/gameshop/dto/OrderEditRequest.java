package com.gameshop.dto;

import lombok.Data;

import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderEditRequest extends OrderRequest {
    private Long userId;
}



