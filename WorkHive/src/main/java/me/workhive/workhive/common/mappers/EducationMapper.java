package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreateEducationRequest;
import me.workhive.workhive.domain.dto.response.EducationResponse;
import me.workhive.workhive.domain.entities.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {
    public Education toEntityCreate(CreateEducationRequest request) {
        return Education.builder()
                .Institution(request.getInstitution())
                .major(request.getMajor())
                .build();
    }

    /*public Education toEntityUpdate(UpdateEducationRequest request, UUID id) {
        return Education.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .isAvailable(request.getIsAvailable())
                .category(request.getCategory())
                .build();
    }*/

    public EducationResponse toDto(Education education){
        return EducationResponse.builder()
                .id(education.getId())
                .institution(education.getInstitution())
                .major(education.getMajor())
                .build();
    }
    
}
