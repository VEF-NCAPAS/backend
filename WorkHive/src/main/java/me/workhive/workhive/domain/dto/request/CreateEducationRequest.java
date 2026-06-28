package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEducationRequest {
    @NotBlank(message = "institution is required")
    private String institution;

    @NotBlank(message = "major is required")
    private String major;

}
