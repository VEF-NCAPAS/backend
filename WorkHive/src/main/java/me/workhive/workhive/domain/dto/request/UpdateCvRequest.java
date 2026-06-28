package me.workhive.workhive.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCvRequest {
    private String professionalSummary;

    private String location;

    private String city;

    private List<CreateExperienceRequest> experiences;

    private List<CreateEducationRequest> education;

    private List<SkillSelection> skills;

    private List<LanguageSelection> languages;
}
