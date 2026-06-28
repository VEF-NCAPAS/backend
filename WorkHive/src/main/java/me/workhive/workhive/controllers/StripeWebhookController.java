package me.workhive.workhive.controllers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature
    ) {

        subscriptionService.handleWebhook(payload, signature);

        return ResponseEntity.ok("Received");
    }

}