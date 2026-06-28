package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import me.workhive.workhive.domain.dto.request.ResendEmailRequest;
import me.workhive.workhive.services.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RestClient resendRestClient;

    @Value("${resend.from}")
    private String from;

    @Override
    public void sendHtmlEmail(String to, String subject, String html) {

        ResendEmailRequest request = new ResendEmailRequest(
                from,
                List.of(to),
                subject,
                html
        );

        try {

            resendRestClient
                    .post()
                    .uri("/emails")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {

            throw new RuntimeException("Error sending email", e);

        }

    }

}