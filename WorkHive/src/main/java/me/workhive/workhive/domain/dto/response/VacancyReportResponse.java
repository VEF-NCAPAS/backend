package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VacancyReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalVacancies;
    private Long activeVacancies;
    private Long closedVacancies;
}