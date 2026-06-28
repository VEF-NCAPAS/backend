package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateExperienceRequest {
    @NotBlank(message = "company name is required")
    private String company;

    @NotBlank(message = "position is required")
    private String position;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "hire date is required")
    @Past(message = "hire date should be in the past")
    private LocalDate hireDate;

    @NotNull(message = "end date is required")
    @Past(message = "end date should be in the past")
    private LocalDate endDate;
}
