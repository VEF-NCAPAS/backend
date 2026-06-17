package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreateExperienceRequest;
import me.workhive.workhive.domain.dto.response.ExperienceResponse;
import me.workhive.workhive.domain.entities.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {
    public Experience toEntityCreate(CreateExperienceRequest request) {
        return Experience.builder()
                .company(request.getCompany())
                .position(request.getPosition())
                .description(request.getDescription())
                .hireDate(request.getHireDate())
                .endDate(request.getEndDate())
                .build();
    }

    /*public Experience toEntityUpdate(UpdateExperienceRequest request, UUID id) {
        return Experience.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .isAvailable(request.getIsAvailable())
                .category(request.getCategory())
                .build();
    }*/

    public ExperienceResponse toDto(Experience experience){
        return ExperienceResponse.builder()
                .id(experience.getId())
                .company(experience.getCompany())
                .position(experience.getPosition())
                .description(experience.getDescription())
                .hireDate(experience.getHireDate())
                .endDate(experience.getEndDate())
                .build();
    }
    
}
