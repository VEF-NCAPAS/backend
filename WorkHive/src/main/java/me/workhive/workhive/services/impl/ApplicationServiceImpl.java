package me.workhive.workhive.services.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.ApplicationMapper;
import me.workhive.workhive.domain.dto.request.CreateApplicationRequest;
import me.workhive.workhive.domain.dto.request.UpdateApplicationStatusRequest;
import me.workhive.workhive.domain.dto.response.ApplicationCandidateResponse;
import me.workhive.workhive.domain.dto.response.ApplicationRecruiterResponse;
import me.workhive.workhive.domain.dto.response.ApplicationResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.*;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.*;
import me.workhive.workhive.services.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final CvRepository cvRepository;
    private final ApplicationMapper applicationMapper;


    @Override
    @Transactional
    public ApplicationResponse createApplication(CreateApplicationRequest request, User user) {
        CandidateProfile candidate = this.findCandidate(user);
        Vacancy vacancy = this.findVacancy(request.getVacancyId());
        Cv cv = cvRepository.findByCandidateProfile(candidate)
                .orElseThrow(() -> new ResourceNotFoundException("No CV found for this candidate"));

        Application application = applicationMapper.toApplicationCreate(request, candidate, vacancy, cv);
        application.setApplicationDate(LocalDate.now());

        if (applicationRepository.existsByCandidateAndVacancy(candidate, vacancy)) {
            throw new BusinessRuleException(
                    "You have already applied to this vacancy"
            );
        }

        if (vacancy.getStatus() != VacancyStatus.OPEN) {
            throw new BusinessRuleException(
                    "This vacancy is not available"
            );
        }

        Application saved = applicationRepository.save(application);
        return applicationMapper.toApplicationCandidateDto(saved);
    }


    @Override
    public ApplicationResponse getApplicationById(UUID id, User user) {
        Application application = findApplication(id);

        if (user.getRole() == Role.CANDIDATE) {
            if (!application.getCandidate().getUser().getId().equals(user.getId())) {
                throw new DeniedAccessException("You cannot view this application");
            }
            return applicationMapper.toApplicationCandidateDto(application);
        }

        if (user.getRole() == Role.RECRUITER) {
            RecruiterProfile recruiter = findRecruiter(user);
            if (!application.getVacancy().getCompany().getId().equals(recruiter.getCompany().getId())) {
                throw new DeniedAccessException("You cannot view applications from another company");
            }
            return applicationMapper.toApplicationRecruiterDto(application);
        }

        throw new DeniedAccessException("Role not authorized");
    }

    @Override
    public PageableResponse<? extends ApplicationResponse> getAllApplications(
            int page,
            int size,
            String sortBy,
            String sortOrder,
            User user
    ) {

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (user.getRole() == Role.CANDIDATE) {

            CandidateProfile candidate = findCandidate(user);

            Page<ApplicationCandidateResponse> applicationPage =
                    applicationMapper.toDtoApplicationCandidateList(
                            applicationRepository.findByCandidate_Id(
                                    candidate.getId(),
                                    pageable
                            )
                    );

            return buildPageResponse(applicationPage);
        }

        else if (user.getRole() == Role.RECRUITER) {

            RecruiterProfile recruiter = findRecruiter(user);

            Page<ApplicationRecruiterResponse> applicationPage =
                    applicationMapper.toDtoApplicationRecruiterList(
                            applicationRepository.findByVacancy_CompanyId(
                                    recruiter.getCompany().getId(),
                                    pageable
                            )
                    );

            return buildPageResponse(applicationPage);
        }

        throw new DeniedAccessException("Role not authorized");
    }


    @Transactional
    @Override
    public ApplicationResponse withdrawApplication(UUID id, User user) {

        Application application = findApplication(id);

        if (!application.getCandidate().getUser().getId().equals(user.getId())) {
            throw new DeniedAccessException("You cannot withdraw this application");
        }

        if (application.getApplicationStatus() == ApplicationStatus.SELECTED ||
                application.getApplicationStatus() == ApplicationStatus.REJECTED) {
            throw new BusinessRuleException(
                    "This application cannot be withdrawn"
            );
        }


        if (application.getApplicationStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BusinessRuleException(
                    "Application has already been withdrawn"
            );
        }

        application.setApplicationStatus(ApplicationStatus.WITHDRAWN);

        return applicationMapper.toApplicationCandidateDto(
                applicationRepository.save(application)
        );
    }

    @Transactional
    @Override
    public ApplicationResponse reviewApplication(UUID id, User user) {
        RecruiterProfile recruiter = findRecruiter(user);
        Application application = findApplication(id);

        if (!application.getVacancy().getCompany().getId().equals(recruiter.getCompany().getId())) {
            throw new DeniedAccessException("You cannot manage applications from another company");
        }

        if (application.getApplicationStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BusinessRuleException(
                    "Withdrawn applications cannot be reviewed"
            );
        }

        if (application.getApplicationStatus() == ApplicationStatus.SELECTED ||
                application.getApplicationStatus() == ApplicationStatus.REJECTED) {
            throw new BusinessRuleException(
                    "This application has already been finalized"
            );
        }

        if (application.getApplicationStatus() != ApplicationStatus.APPLIED) {
            throw new BusinessRuleException(
                    "Only applied applications can be reviewed"
            );
        }

        application.setApplicationStatus(
                ApplicationStatus.REVIEWED
        );

        return applicationMapper.toApplicationRecruiterDto(
                applicationRepository.save(application)
        );
    }

    @Transactional
    @Override
    public ApplicationResponse updateApplicationStatus(UUID id, UpdateApplicationStatusRequest request, User user) {

        RecruiterProfile recruiter = findRecruiter(user);

        Application application = findApplication(id);

        if (!application.getVacancy().getCompany().getId()
                .equals(recruiter.getCompany().getId())) {

            throw new DeniedAccessException("You cannot manage applications from another company");
        }


        if (application.getApplicationStatus() == ApplicationStatus.SELECTED ||
                application.getApplicationStatus() == ApplicationStatus.REJECTED ||
                application.getApplicationStatus() == ApplicationStatus.WITHDRAWN) {

            throw new BusinessRuleException(
                    "This application has already been finalized"
            );
        }

        if (application.getApplicationStatus() != ApplicationStatus.TECHNICAL_TEST) {
            throw new BusinessRuleException(
                    "Application must complete the technical test stage first"
            );
        }

        if (request.getStatus() != ApplicationStatus.SELECTED &&
                request.getStatus() != ApplicationStatus.REJECTED) {

            throw new BusinessRuleException(
                    "Only SELECTED or REJECTED are allowed"
            );
        }

        application.setApplicationStatus(request.getStatus());

        return applicationMapper.toApplicationRecruiterDto(
                applicationRepository.save(application)
        );
    }
    private Application findApplication(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private CandidateProfile findCandidate(User user) {
        return candidateRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));
    }

    private RecruiterProfile findRecruiter(User user) {
        return recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
    }

    private Vacancy findVacancy(UUID id) {
        return vacancyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vacancy not found"));
    }

    private <T> PageableResponse<T> buildPageResponse(Page<T> page) {

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No applications found");
        }

        return PageableResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

}
