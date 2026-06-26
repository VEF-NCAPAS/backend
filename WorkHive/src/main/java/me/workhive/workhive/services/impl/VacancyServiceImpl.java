package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.VacancyMapper;
import me.workhive.workhive.domain.dto.request.CreateVacancyRequest;
import me.workhive.workhive.domain.dto.request.RequirementSelection;
import me.workhive.workhive.domain.dto.request.UpdateVacancyRequest;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.TopVacancyResponse;
import me.workhive.workhive.domain.dto.response.VacancyReportResponse;
import me.workhive.workhive.domain.dto.response.VacancyResponse;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.Vacancy;
import me.workhive.workhive.domain.entities.enums.Modality;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.RequirementRepository;
import me.workhive.workhive.repositories.VacancyRepository;
import me.workhive.workhive.services.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static me.workhive.workhive.utils.DateUtils.*;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    public final VacancyRepository vacancyRepository;
    private final RequirementRepository requirementRepository;
    private final RecruiterRepository recruiterRepository;
    public final VacancyMapper vacancyMapper;
    @Override
    @Transactional
    public VacancyResponse createVacancy(CreateVacancyRequest request, User user) {

        RecruiterProfile recruiterProfile = this.findRecruiter(user);

        List<UUID> requirementIds = request.getRequirements()
                .stream()
                .map(RequirementSelection::getId)
                .toList();

        List<Requirement> requirements = requirementRepository.findAllById(requirementIds);
        Vacancy vacancy = vacancyMapper.toVacancyCreate(request, requirements);
        vacancy.setCompany(recruiterProfile.getCompany());
        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        return vacancyMapper.toVacancyDto(savedVacancy);

    }

    @Override
    public PageableResponse<VacancyResponse> getAllVacancies(int page, int size, String sortBy, String sortOrder,
                                                             String title, Modality modality, User user
    ) {

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<Vacancy> vacancies;

        // candidate
        if (user.getRole() == Role.CANDIDATE) {
            if (title != null && modality != null) {
                vacancies = vacancyRepository.findByTitleContainingIgnoreCaseAndModalityAndStatus(
                                title,
                                modality,
                                VacancyStatus.OPEN,
                                pageable);

            } else if (title != null) {
                vacancies = vacancyRepository.findByTitleContainingIgnoreCaseAndStatus(
                                title,
                                VacancyStatus.OPEN,
                                pageable);
            } else if (modality != null) {
                vacancies = vacancyRepository.findByModalityAndStatus(
                                modality,
                                VacancyStatus.OPEN,
                                pageable);
            } else {
                vacancies = vacancyRepository.findByStatus(
                                VacancyStatus.OPEN,
                                pageable);
            }
        }

        // recruiter
        else if (user.getRole() == Role.RECRUITER){
            RecruiterProfile recruiter = this.findRecruiter(user);
            if (title != null && modality != null) {
                vacancies = vacancyRepository.findByCompanyAndTitleContainingIgnoreCaseAndModality(
                                recruiter.getCompany(),
                                title,
                                modality,
                                pageable);

            } else if (title != null) {

                vacancies = vacancyRepository.findByCompanyAndTitleContainingIgnoreCase(
                                recruiter.getCompany(),
                                title,
                                pageable);

            } else if (modality != null) {

                vacancies = vacancyRepository.findByCompanyAndModality(
                                recruiter.getCompany(),
                                modality,
                                pageable);

            } else {

                vacancies = vacancyRepository.findByCompany(
                                recruiter.getCompany(),
                                pageable);
            }
        }else {

            throw new DeniedAccessException(
                    "Role not authorized"
            );
        }

        Page<VacancyResponse> vacancyPage = vacancyMapper.toDtoVacancyList(vacancies);

        if (vacancyPage.getTotalElements() == 0) {

            throw new ResourceNotFoundException("No vacancies found");
        }

        return PageableResponse.<VacancyResponse>builder()
                .content(vacancyPage.getContent())
                .page(vacancyPage.getNumber())
                .size(vacancyPage.getSize())
                .totalElements(vacancyPage.getTotalElements())
                .last(vacancyPage.isLast())
                .build();
    }
    @Override
    public VacancyResponse getVacancyById(UUID id){
        return vacancyMapper.toVacancyDto(
                this.findVacancyById(id)
        );
    }
    @Override
    @Transactional
    public VacancyResponse updateVacancy(UUID id, UpdateVacancyRequest request, User user) {

        Vacancy existsVacancy = findVacancyById(id);

        RecruiterProfile recruiter = this.findRecruiter(user);

        if (!existsVacancy.getCompany().getId()
                .equals(recruiter.getCompany().getId())) {
            throw new DeniedAccessException("You cannot edit vacancies from another company");
        }
        List<UUID> requirementIds = request.getRequirements()
                .stream()
                .map(RequirementSelection::getId)
                .toList();

        List<Requirement> requirements = requirementRepository.findAllById(requirementIds);

        return vacancyMapper.toVacancyDto(vacancyRepository
                .save(vacancyMapper.toVacancyUpdate(request, id, requirements, existsVacancy)));
    }

    @Override
    public VacancyResponse deleteVacancy(UUID id,  User user){
        Vacancy vacancy = this.findVacancyById(id);
        RecruiterProfile recruiter = this.findRecruiter(user);

        if (!vacancy.getCompany().getId()
                .equals(recruiter.getCompany().getId())) {
            throw new DeniedAccessException(
                    "You cannot delete vacancies from another company"
            );
        }
        VacancyResponse response = vacancyMapper.toVacancyDto(vacancy);
        vacancyRepository.delete(vacancy);
        return response;
    }
    @Override
    public List<TopVacancyResponse> getTopVacancies(User user){

        RecruiterProfile recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        return vacancyRepository.findMostAppliedByCompany(
                recruiter.getCompany().getId(),
                PageRequest.of(0,5)
        );
    }

    @Override
    public VacancyReportResponse getVacancyVolume(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        LocalDateTime fromDateTime = startOfDay(startDate);
        LocalDateTime toDateTime = endOfDay(endDate);

        return VacancyReportResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalVacancies(vacancyRepository.countByCreatedAtBetween(fromDateTime, toDateTime))
                .activeVacancies(vacancyRepository.countByCreatedAtBetweenAndStatus(fromDateTime, toDateTime, VacancyStatus.OPEN))
                .closedVacancies(vacancyRepository.countByCreatedAtBetweenAndStatus(fromDateTime, toDateTime, VacancyStatus.CLOSE))
                .build();
    }

    public Vacancy findVacancyById(UUID id){
        return vacancyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vacancy not found")
                );
    }

    public RecruiterProfile findRecruiter(User user){
        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
    }

}


