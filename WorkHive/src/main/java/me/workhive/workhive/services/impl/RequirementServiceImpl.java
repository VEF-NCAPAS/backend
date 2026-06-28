package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.RequirementMapper;
import me.workhive.workhive.domain.dto.response.RequirementResponse;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.RequirementRepository;
import me.workhive.workhive.services.RequirementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;
    private final RequirementMapper requirementMapper;

    @Override
    public List<RequirementResponse> getAllRequirements() {

        List<Requirement> requirements = requirementRepository.findAll();

        if (requirements.isEmpty()) {
            throw new ResourceNotFoundException("No requirements found");
        }
        return requirements.stream()
                .map(requirementMapper::toRequirementDto)
                .toList();
    }

    @Override
    public RequirementResponse getRequirementById(UUID id) {

        Requirement requirement = requirementRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Requirement not found")
                        );

        return requirementMapper.toRequirementDto(requirement);
    }
}