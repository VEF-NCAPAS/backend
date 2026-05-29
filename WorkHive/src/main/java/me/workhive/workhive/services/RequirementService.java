package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.response.RequirementResponse;

import java.util.List;
import java.util.UUID;

public interface RequirementService {

    List<RequirementResponse> getAllRequirements();

    RequirementResponse getRequirementById(UUID id);
}
