package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserGrowthItemResponse {
    private String period;
    private long candidates;
    private long recruiters;
}