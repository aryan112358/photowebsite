package com.photowebsite.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String amount;
    private String currency;
    private String paymentMethod;
    private String description;
    // Add other necessary payment details
}