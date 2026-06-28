package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.domain.entities.enums.Modality;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateVacancyRequest {
    private String title;
    private String description;
    private List<RequirementSelection> requirements;
    private BigDecimal salary;
    private Modality modality;
    private VacancyStatus status;
}
