package me.workhive.workhive.domain.dto.request;

import java.util.List;

public record ResendEmailRequest(

        String from,
        List<String> to,
        String subject,
        String html

) {}