package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateTechnicalTestRequest {
    @NotNull(message = "Application is required")
    private UUID applicationId;

    @NotBlank(message = "The link is required")
    @Pattern(
            regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
            message = "The link must be a valid URL starting with http:// or https://"
    )
    private String externalLink;

    @NotNull(message = "The deadline is required")
    @Future(message = "The deadline must be in the future")
    private LocalDateTime deadline;
}
