package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopVacancyResponse {

    private UUID vacancyId;
    private String title;
    private Long totalApplications;

}