package com.photowebsite.controller;

import com.photowebsite.dto.request.PaymentRequest;
import com.photowebsite.dto.response.PaymentResponse;
import com.photowebsite.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "Payment management APIs")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Initiate payment for photo")
    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(
            @Valid @RequestBody PaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(paymentService.initiatePayment(request, userDetails.getUsername()));
    }

    @Operation(summary = "Get user payments")
    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getUserPayments(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(paymentService.getUserPayments(userDetails.getUsername(), pageable));
    }

    @Operation(summary = "Handle Stripe webhook")
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        paymentService.handlePaymentWebhook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
