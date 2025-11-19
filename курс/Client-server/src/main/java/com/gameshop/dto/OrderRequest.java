package com.gameshop.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String paymentMethod;
    private String paymentType;
    private String deliveryAddress;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;
}





