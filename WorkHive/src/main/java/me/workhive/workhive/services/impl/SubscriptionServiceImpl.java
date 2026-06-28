package me.workhive.workhive.services.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.response.StripeResponse;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.SubscriptionPlan;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final CompanyRepository companyRepository;
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.premium-price-id}")
    private String premiumPriceId;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final RecruiterRepository recruiterRepository;

    @Override
    public StripeResponse createCheckoutSession(User user) {

        if (!user.getRole().name().equals("RECRUITER")) {
            throw new DeniedAccessException(
                    "Only recruiters can purchase Premium."
            );
        }

        RecruiterProfile recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recruiter not found"));

        Company company = recruiter.getCompany();

        Stripe.apiKey = secretKey;

        try {

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)

                    .setSuccessUrl(
                            frontendUrl + "/reclutador/payment/success?session_id={CHECKOUT_SESSION_ID}"
                    )

                    .setCancelUrl(
                            frontendUrl + "/reclutador/payment/cancel"
                    )

                    .setClientReferenceId(company.getId().toString())

                    .addLineItem(

                            SessionCreateParams.LineItem.builder()
                                    .setPrice(premiumPriceId)
                                    .setQuantity(1L)
                                    .build()

                    )

                    .build();



            Session session = Session.create(params);

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Checkout created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {

            throw new RuntimeException(e.getMessage());

        }

    }

    @Override
    public void handleWebhook(String payload, String signature) {

        Event event;

        try {

            event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret
            );

        } catch (Exception e) {

            throw new RuntimeException("Invalid webhook");

        }

        if ("checkout.session.completed".equals(event.getType())) {

            String rawJson = event.getDataObjectDeserializer().getRawJson();

            JsonObject json = JsonParser
                    .parseString(rawJson)
                    .getAsJsonObject();

            String companyId = json.get("client_reference_id").getAsString();
            String paymentIntent = json.get("payment_intent").getAsString();

            Company company = companyRepository.findById(
                    UUID.fromString(companyId)
            ).orElseThrow(() ->
                    new ResourceNotFoundException("Company not found"));

            company.setSubscriptionPlan(SubscriptionPlan.PREMIUM);

            company.setStripePaymentIntentId(paymentIntent);

            companyRepository.save(company);

            System.out.println("Empresa actualizada a PREMIUM");
        }

    }
}