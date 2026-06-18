package me.workhive.workhive.common.mappers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateApplicationRequest;
import me.workhive.workhive.domain.dto.response.ApplicationCandidateResponse;
import me.workhive.workhive.domain.dto.response.ApplicationRecruiterResponse;
import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Cv;
import me.workhive.workhive.domain.entities.Vacancy;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapper {
    private final TechnicalTestMapper technicalTestMapper;
    private final InterviewMapper interviewMapper;
    private final CvMapper cvMapper;
    public Application toApplicationCreate(CreateApplicationRequest request, CandidateProfile candidate,
                                           Vacancy vacancy, Cv cv) {
        return Application.builder()
                .candidate(candidate)
                .vacancy(vacancy)
                .cv(cv)
                .coverLetter(request.getCoverLetter())
                .applicationStatus(ApplicationStatus.APPLIED)
                .build();
    }

    public ApplicationCandidateResponse toApplicationCandidateDto(Application application){
        return ApplicationCandidateResponse.builder()
                .id(application.getId())
                .vacancyTitle(application.getVacancy().getTitle())
                .companyName(application.getVacancy().getCompany().getName())
                .applicationStatus(application.getApplicationStatus())
                .applicationDate(application.getApplicationDate())
                .technicalTest(application.getTechnicalTest() != null ?
                        technicalTestMapper.toTechnicalTestDto(application.getTechnicalTest()) : null)
                .interview(application.getInterview() != null ?
                        interviewMapper.toInterviewDto(application.getInterview()) : null)
                .build();

    }

    public ApplicationRecruiterResponse toApplicationRecruiterDto(Application application){
        return ApplicationRecruiterResponse.builder()
                .id(application.getId())
                .candidateName(application.getCandidate().getUser().getName())
                .candidateEmail(application.getCandidate().getUser().getEmail())
                .vacancyTitle(application.getVacancy().getTitle())
                .coverLetter(application.getCoverLetter())
                .cv(cvMapper.toCvDto(application.getCv()))
                .applicationStatus(application.getApplicationStatus())
                .applicationDate(application.getApplicationDate())
                .technicalTest(application.getTechnicalTest() != null ?
                        technicalTestMapper.toTechnicalTestDto(application.getTechnicalTest()) : null)
                .interview(application.getInterview() != null ?
                        interviewMapper.toInterviewDto(application.getInterview()) : null)
                .build();

    }

    public Page<ApplicationCandidateResponse> toDtoApplicationCandidateList(Page<Application> applications){
        return applications
                .map(this::toApplicationCandidateDto);
    }

    public Page<ApplicationRecruiterResponse> toDtoApplicationRecruiterList(Page<Application> applications){
        return applications
                .map(this::toApplicationRecruiterDto);
    }
}
