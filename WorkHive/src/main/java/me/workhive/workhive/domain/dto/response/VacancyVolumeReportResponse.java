package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VacancyVolumeReportResponse {
    private LocalDate from;
    private LocalDate to;
    private long created;
    private long active;
    private long closed;
}
