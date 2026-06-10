package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.TechnicalTestMapper;
import me.workhive.workhive.domain.dto.request.CreateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.request.UpdateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.response.TechnicalTestResponse;
import me.workhive.workhive.domain.entities.*;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.ApplicationRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.TechnicalTestRepository;
import me.workhive.workhive.services.EmailService;
import me.workhive.workhive.services.EmailTemplateService;
import me.workhive.workhive.services.TechnicalTestService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TechnicalTestServiceImpl implements TechnicalTestService {

    private final TechnicalTestRepository technicalTestRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepository recruiterRepository;
    private final TechnicalTestMapper technicalTestMapper;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;


    @Transactional
    @Override
    public TechnicalTestResponse createTechnicalTest(CreateTechnicalTestRequest request, User user) {
        RecruiterProfile recruiter = findRecruiter(user);
        Application application = findApplication(request.getApplicationId());

        if (!application.getVacancy().getCompany().getId().equals(recruiter.getCompany().getId())) {
            throw new DeniedAccessException(
                    "You cannot manage applications from another company"
            );
        }

        if (application.getApplicationStatus() != ApplicationStatus.INTERVIEW) {
            throw new BusinessRuleException(
                    "Application must be interviewed first"
            );
        }

        if (application.getTechnicalTest() != null) {
            throw new BusinessRuleException(
                    "Technical test already exists for this application"
            );
        }

        TechnicalTest technicalTest = technicalTestMapper.toTechnicalTestCreate(request, application);
        application.setApplicationStatus(ApplicationStatus.TECHNICAL_TEST);
        applicationRepository.save(application);
        TechnicalTest savedTest = technicalTestRepository.save(technicalTest);
        String html = emailTemplateService.technicalTestTemplate(
                application.getCandidate().getUser().getName(),
                application.getVacancy().getTitle(),
                savedTest.getLink(),
                savedTest.getDeadline()
        );

        emailService.sendHtmlEmail(
                application.getCandidate().getUser().getEmail(),
                "Prueba técnica asignada",
                html
        );
        return technicalTestMapper.toTechnicalTestDto (savedTest);
    }

    @Transactional
    @Override
    public TechnicalTestResponse updateTechnicalTest(UUID id, UpdateTechnicalTestRequest request, User user) {

        RecruiterProfile recruiter = findRecruiter(user);

        TechnicalTest technicalTest = findTechnicalTest(id);

        if (!technicalTest.getApplication()
                .getVacancy()
                .getCompany()
                .getId()
                .equals(recruiter.getCompany().getId())) {

            throw new DeniedAccessException(
                    "You cannot edit technical test from another company"
            );
        }

        if (technicalTest.getApplication().getApplicationStatus() == ApplicationStatus.WITHDRAWN) {

            throw new BusinessRuleException(
                    "Application was withdrawn"
            );
        }

        TechnicalTest updatedTechnicalTest = technicalTestMapper.toTechnicalTestUpdate(request, id, technicalTest);

        return technicalTestMapper.toTechnicalTestDto(
                technicalTestRepository.save(updatedTechnicalTest)
        );
    }


    private RecruiterProfile findRecruiter(User user) {
        return recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
    }

    private Application findApplication(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private TechnicalTest findTechnicalTest(UUID id){
        return  technicalTestRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Technical test not found"));
    }

}
