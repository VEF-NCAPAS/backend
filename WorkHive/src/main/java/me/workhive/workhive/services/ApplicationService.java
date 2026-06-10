package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreateApplicationRequest;
import me.workhive.workhive.domain.dto.request.UpdateApplicationStatusRequest;
import me.workhive.workhive.domain.dto.response.ApplicationResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.User;

import java.util.UUID;

public interface ApplicationService {
    ApplicationResponse createApplication(CreateApplicationRequest request, User user);
    ApplicationResponse getApplicationById(UUID id, User user);
    PageableResponse<? extends ApplicationResponse> getAllApplications(
            int page,
            int size,
            String sortBy,
            String sortOrder,
            User user
    );
    PageableResponse<? extends ApplicationResponse> getApplicationsByVacancy(
            UUID vacancyId,
            String skill,
            int page,
            int size,
            User user
    );

    ApplicationResponse withdrawApplication(UUID id, User user);
    ApplicationResponse reviewApplication(UUID id, User user);
    ApplicationResponse updateApplicationStatus(UUID id, UpdateApplicationStatusRequest request, User user);
}
