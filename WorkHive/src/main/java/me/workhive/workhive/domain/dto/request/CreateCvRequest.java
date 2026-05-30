package me.workhive.workhive.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CreateCvRequest {
    private List<CreateExperienceRequest> experiences;

    private List<CreateEducationRequest> education;

    private List<SkillSelection> skills;
}
