package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateApplicationRequest {
    @NotNull(message = "Select vacancy to apply")
    private UUID vacancyId;
    private String coverLetter;
}
