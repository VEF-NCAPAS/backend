package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRecruiterResponse implements ApplicationResponse{
    private UUID id;
    private String candidateName;
    private String candidateEmail;
    private String vacancyTitle;
    private CvResponse cv;
    private String coverLetter;
    private ApplicationStatus applicationStatus;
    private LocalDate applicationDate;
    private LocalDateTime selectedDate;
    private InterviewResponse interview;
    private TechnicalTestResponse technicalTest;
}