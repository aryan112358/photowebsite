package com.photowebsite.dto.response;

import com.photowebsite.entity.Payment;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private String transactionId;
    private Long photoId;
    private String buyerUsername;
    private String sellerUsername;
    private BigDecimal amount;
    private String currency;
    private Payment.PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String clientSecret;  // For Stripe integration
} 