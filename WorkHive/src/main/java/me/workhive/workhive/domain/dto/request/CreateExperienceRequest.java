package me.workhive.workhive.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateExperienceRequest {
    private String company;

    private String position;

    private String description;

    private LocalDate hireDate;

    private LocalDate endDate;
}
