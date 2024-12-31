package com.photowebsite.service;

import com.photowebsite.dto.request.PaymentRequest;
import com.photowebsite.dto.response.PaymentResponse;
import com.photowebsite.entity.Payment;
import com.photowebsite.entity.Photo;
import com.photowebsite.exception.PaymentException;
import com.photowebsite.exception.ResourceNotFoundException;
import com.photowebsite.repository.PaymentRepository;
import com.photowebsite.repository.PhotoRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PhotoRepository photoRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request, String buyerUsername) {
        Photo photo = photoRepository.findById(request.getPhotoId())
            .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        if (photo.getUploadedBy().equals(buyerUsername)) {
            throw new PaymentException("You cannot purchase your own photo");
        }

        try {
            Stripe.apiKey = stripeSecretKey;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", photo.getPrice().multiply(new java.math.BigDecimal("100")).intValue());
            params.put("currency", request.getCurrency());
            params.put("payment_method_types", java.util.Arrays.asList("card"));
            params.put("metadata", Map.of(
                "photo_id", photo.getId().toString(),
                "buyer_username", buyerUsername,
                "seller_username", photo.getUploadedBy()
            ));

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Payment payment = new Payment();
            payment.setTransactionId(paymentIntent.getId());
            payment.setPhoto(photo);
            payment.setBuyerUsername(buyerUsername);
            payment.setSellerUsername(photo.getUploadedBy().getUsername());
            payment.setAmount(photo.getPrice());
            payment.setCurrency(request.getCurrency());
            payment.setStatus(Payment.PaymentStatus.PENDING);

            Payment savedPayment = paymentRepository.save(payment);

            return PaymentResponse.builder()
                .id(savedPayment.getId())
                .transactionId(savedPayment.getTransactionId())
                .photoId(photo.getId())
                .buyerUsername(buyerUsername)
                .sellerUsername(photo.getUploadedBy().getUsername())
                .amount(savedPayment.getAmount())
                .currency(savedPayment.getCurrency())
                .status(savedPayment.getStatus())
                .createdAt(savedPayment.getCreatedAt())
                .clientSecret(paymentIntent.getClientSecret())
                .build();

        } catch (StripeException e) {
            throw new PaymentException("Failed to initiate payment: " + e.getMessage());
        }
    }

    @Transactional
    public void handlePaymentWebhook(String payload, String sigHeader) {
        // Implement Stripe webhook handling
        // Verify webhook signature
        // Update payment status
        // Grant access to purchased photo
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getUserPayments(String username, Pageable pageable) {
        return paymentRepository.findByBuyerUsernameOrSellerUsername(username, username, pageable)
            .map(this::mapToPaymentResponse);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
            .id(payment.getId())
            .transactionId(payment.getTransactionId())
            .photoId(payment.getPhoto().getId())
            .buyerUsername(payment.getBuyerUsername())
            .sellerUsername(payment.getSellerUsername())
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .status(payment.getStatus())
            .createdAt(payment.getCreatedAt())
            .completedAt(payment.getCompletedAt())
            .build();
    }
}