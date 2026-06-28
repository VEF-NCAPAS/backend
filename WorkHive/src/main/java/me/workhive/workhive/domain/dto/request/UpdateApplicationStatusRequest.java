package me.workhive.workhive.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;

@Data
public class UpdateApplicationStatusRequest {
    @NotNull(message = "The final decision status is required")
    @Schema(example = "SELECTED")
    private ApplicationStatus status;
}
