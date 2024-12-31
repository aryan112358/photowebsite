package com.photowebsite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @PostMapping("/payment")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody String webhookPayload) {
        return ResponseEntity.ok("Payment webhook received: " + webhookPayload);
    }
}
