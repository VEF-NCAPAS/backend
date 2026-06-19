package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ApplicationVolumeReportResponse {
    private LocalDate from;
    private LocalDate to;
    private long totalApplications;
}
