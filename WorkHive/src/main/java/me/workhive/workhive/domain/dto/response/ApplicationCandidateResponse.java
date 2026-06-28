package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCandidateResponse implements ApplicationResponse {
    private UUID id;
    private String vacancyTitle;
    private String companyName;
    private ApplicationStatus applicationStatus;
    private LocalDate applicationDate;
    private InterviewResponse interview;
    private TechnicalTestResponse technicalTest;
}