package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.request.UpdateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.response.TechnicalTestResponse;
import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.TechnicalTest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TechnicalTestMapper {
    public TechnicalTest toTechnicalTestCreate(CreateTechnicalTestRequest request, Application application){
        return TechnicalTest.builder()
                .application(application)
                .link(request.getLink())
                .deadline(request.getDeadline())
                .build();
    }

    public TechnicalTest toTechnicalTestUpdate(UpdateTechnicalTestRequest request, UUID id,
                                               TechnicalTest existingTechnicalTest
    ) {
        return TechnicalTest.builder()
                .id(id)
                .application(existingTechnicalTest.getApplication())
                .link(request.getExternalLink())
                .deadline(request.getDeadline())
                .build();
    }

    public TechnicalTestResponse toTechnicalTestDto(TechnicalTest technicalTest){
        if (technicalTest == null) {
            return null;
        }
        return TechnicalTestResponse.builder()
                .id(technicalTest.getId())
                .link(technicalTest.getLink())
                .deadline(technicalTest.getDeadline())
                .build();
    }
}
