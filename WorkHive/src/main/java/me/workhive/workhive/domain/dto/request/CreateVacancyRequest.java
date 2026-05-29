package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Modality;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateVacancyRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Requirements are required")
    private List<RequirementSelection> requirements;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private BigDecimal salary;

    @NotNull(message = "Modality is required")
    private Modality modality;

    @NotNull(message = "Vacancy is required")
    private VacancyStatus status;
}
