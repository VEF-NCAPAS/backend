package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.SubscriptionService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final ResponseFactory responseFactory;


    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(
            summary = "Crea sesion de pago",
            description = "Crea la sesion de pago para el plan premium"
    )
    @PostMapping("/checkout")
    public ResponseEntity<GeneralResponse> checkout(@AuthenticationPrincipal User user) {

        return responseFactory.buildResponse(
                "Pay session created successfully",
                HttpStatus.CREATED,
                subscriptionService.createCheckoutSession(user)
        );

    }
}