package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreateVacancyRequest;
import me.workhive.workhive.domain.dto.request.UpdateVacancyRequest;
import me.workhive.workhive.domain.dto.response.VacancyResponse;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.domain.entities.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VacancyMapper {
    public Vacancy toVacancyCreate(CreateVacancyRequest request, List<Requirement> requirements){
        return Vacancy.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(requirements)
                .salary(request.getSalary())
                .modality(request.getModality())
                .status(request.getStatus())
                .build();
    }

    public Vacancy toVacancyUpdate(UpdateVacancyRequest request, UUID id, List<Requirement> requirements, Vacancy existingVacancy) {
        return Vacancy.builder()
                .id(id)
                .company(existingVacancy.getCompany())
                .createdAt(existingVacancy.getCreatedAt())
                .publicationDate(existingVacancy.getPublicationDate())
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(requirements)
                .salary(request.getSalary())
                .modality(request.getModality())
                .status(request.getStatus())
                .build();
    }
    public VacancyResponse toVacancyDto(Vacancy vacancy){
        return VacancyResponse.builder()
                .id(vacancy.getId())
                .companyName(vacancy.getCompany() != null ? vacancy.getCompany().getName() : null)                .title(vacancy.getTitle())
                .description(vacancy.getDescription())
                .requirements(vacancy.getRequirements() != null ?
                        vacancy.getRequirements().stream()
                        .map(Requirement::getName)
                        .collect(Collectors.toList()) : null)
                .salary(vacancy.getSalary())
                .modality(vacancy.getModality())
                .status(vacancy.getStatus())
                .publicationDate(vacancy.getPublicationDate())
                .build();
    }

    public Page<VacancyResponse> toDtoVacancyList(Page<Vacancy> vacancies){
        return vacancies
                .map(this::toVacancyDto);
    }

}
