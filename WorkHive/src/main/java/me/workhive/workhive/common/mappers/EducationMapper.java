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

    public EducationResponse toDto(Education education){
        return EducationResponse.builder()
                .id(education.getId())
                .institution(education.getInstitution())
                .major(education.getMajor())
                .build();
    }
    
}
