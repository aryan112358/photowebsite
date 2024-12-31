package com.photowebsite.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Photo ID is required")
    private Long photoId;
    
    private String paymentMethodId;
    private String currency = "USD";
} 