package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.request.UpdateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.response.TechnicalTestResponse;
import me.workhive.workhive.domain.entities.User;

import java.util.UUID;

public interface TechnicalTestService {
    TechnicalTestResponse createTechnicalTest(CreateTechnicalTestRequest request, User user);
    TechnicalTestResponse updateTechnicalTest(UUID id, UpdateTechnicalTestRequest request, User user);
}
