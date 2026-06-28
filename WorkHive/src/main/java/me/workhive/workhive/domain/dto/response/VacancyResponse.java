package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.dto.request.RequirementSelection;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.domain.entities.enums.Modality;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacancyResponse {
    private UUID id;
    private String companyName;
    private String title;
    private String description;
    private List<String> requirements;
    private BigDecimal salary;
    private Modality modality;
    private VacancyStatus status;
    private LocalDate publicationDate;
}
