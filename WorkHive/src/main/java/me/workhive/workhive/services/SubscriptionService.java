package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.response.StripeResponse;
import me.workhive.workhive.domain.entities.User;

public interface SubscriptionService {

    StripeResponse createCheckoutSession(User user);
    void handleWebhook(String payload, String signature);
}