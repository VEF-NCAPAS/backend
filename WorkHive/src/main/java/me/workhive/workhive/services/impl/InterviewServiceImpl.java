package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.InterviewMapper;
import me.workhive.workhive.domain.dto.request.CreateInterviewRequest;
import me.workhive.workhive.domain.dto.request.UpdateInterviewRequest;
import me.workhive.workhive.domain.dto.response.InterviewResponse;
import me.workhive.workhive.domain.entities.*;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.ApplicationRepository;
import me.workhive.workhive.repositories.InterviewRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.services.InterviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepository recruiterRepository;
    private final InterviewMapper interviewMapper;

    @Transactional
    @Override
    public InterviewResponse createInterview(CreateInterviewRequest request, User user) {

        RecruiterProfile recruiter = findRecruiter(user);

        Application application = findApplication(request.getApplicationId());

        if (!application.getVacancy().getCompany().getId().equals(recruiter.getCompany().getId())) {
            throw new DeniedAccessException(
                    "You cannot manage applications from another company"
            );
        }

        if (application.getApplicationStatus() != ApplicationStatus.REVIEWED) {
            throw new BusinessRuleException(
                    "Application must be reviewed first"
            );
        }

        if (application.getInterview() != null) {
            throw new BusinessRuleException(
                    "Interview already exists for this application"
            );
        }

        Interview interview = interviewMapper.toInterviewCreate(request, application);

        application.setApplicationStatus(ApplicationStatus.INTERVIEW);

        applicationRepository.save(application);

        return interviewMapper.toInterviewDto(interviewRepository.save(interview));
    }

    @Transactional
    @Override
    public InterviewResponse updateInterview(UUID id, UpdateInterviewRequest request, User user) {

        RecruiterProfile recruiter = findRecruiter(user);

        Interview interview = findInterview(id);

        if (!interview.getApplication()
                .getVacancy()
                .getCompany()
                .getId()
                .equals(recruiter.getCompany().getId())) {

            throw new DeniedAccessException(
                    "You cannot edit interviews from another company"
            );
        }

        if (interview.getApplication().getApplicationStatus() == ApplicationStatus.WITHDRAWN) {

            throw new BusinessRuleException(
                    "Application was withdrawn"
            );
        }

        Interview updatedInterview = interviewMapper.toInterviewUpdate(request, id, interview);
        return interviewMapper.toInterviewDto(interviewRepository.save(updatedInterview));
    }


    private RecruiterProfile findRecruiter(User user) {
        return recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
    }

    private Application findApplication(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private Interview findInterview(UUID id){
        return interviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }
}
