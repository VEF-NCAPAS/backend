package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePrivateCommentRequest {

    @NotBlank(message = "The description is required")
    private String description;
}