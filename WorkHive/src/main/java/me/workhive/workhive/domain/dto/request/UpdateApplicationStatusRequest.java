package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;

@Data
public class UpdateApplicationStatusRequest {
    @NotNull(message = "The final decision status is required")
    private ApplicationStatus status;
}
