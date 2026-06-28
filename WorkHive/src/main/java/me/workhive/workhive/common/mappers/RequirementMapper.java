package me.workhive.workhive.common.mappers;


import me.workhive.workhive.domain.dto.response.RequirementResponse;
import me.workhive.workhive.domain.entities.Requirement;
import org.springframework.stereotype.Component;

@Component
public class RequirementMapper {
    public RequirementResponse toRequirementDto(Requirement requirement) {
        return RequirementResponse.builder()
                .id(requirement.getId())
                .name(requirement.getName())
                .build();
    }
}
