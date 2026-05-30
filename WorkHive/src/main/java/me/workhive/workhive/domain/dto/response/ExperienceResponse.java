package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceResponse {
    private UUID id;

    private String Company;

    private String position;

    private String description;

    private LocalDate HireDate;

    private LocalDate EndDate;
}
