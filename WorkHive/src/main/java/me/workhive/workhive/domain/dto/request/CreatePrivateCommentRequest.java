package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePrivateCommentRequest {
    @NotBlank(message = "The description is required")
    private String description;

    @NotNull(message = "application ID is required")
    private UUID applicationId;

}
