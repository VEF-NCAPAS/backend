package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/webhook")
    @Operation(
            summary = "Recibir webhook de Stripe",
            description = "Recibe y procesa los eventos enviados por Stripe para actualizar el estado de las suscripciones y pagos."
    )
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature
    ) {

        subscriptionService.handleWebhook(payload, signature);

        return ResponseEntity.ok("Received");
    }

}