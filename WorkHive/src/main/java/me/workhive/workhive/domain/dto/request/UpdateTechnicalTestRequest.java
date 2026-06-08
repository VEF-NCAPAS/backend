package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTechnicalTestRequest {
    @Pattern(
            regexp = "^https?:\\/\\/[\\w\\.-]+\\.[a-z]{2,}(\\/[\\w\\.-]*)*\\/?$",
            message = "The link must be a valid URL starting with http:// or https://"
    )
    private String externalLink;
    @Future(message = "The deadline must be in the future")
    private LocalDateTime deadline;

}
