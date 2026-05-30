package me.workhive.workhive.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCvRequest {
    private List<CreateExperienceRequest> experiences;

    private List<CreateEducationRequest> education;

    private List<SkillSelection> skills;
}
