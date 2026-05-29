package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreateVacancyRequest;
import me.workhive.workhive.domain.dto.request.UpdateVacancyRequest;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.VacancyResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Modality;

import java.util.UUID;

public interface VacancyService {
    VacancyResponse createVacancy(CreateVacancyRequest request, User user);
    PageableResponse<VacancyResponse> getAllVacancies( int page, int size, String sortBy, String sortOrder,
                                                       String title, Modality modality, User user);
    VacancyResponse getVacancyById(UUID id);
    VacancyResponse updateVacancy(UUID  id , UpdateVacancyRequest request, User user);
    VacancyResponse deleteVacancy(UUID id,  User user);
}
