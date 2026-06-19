package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserGrowthReportResponse {
    private LocalDate from;
    private LocalDate to;
    private String groupBy;
    private List<UserGrowthItemResponse> growth;
}
