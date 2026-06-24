package me.workhive.workhive.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateCvRequest {
    @NotBlank(message = "The professional summary is required")
    private String professionalSummary;

    @NotBlank(message = "The location is required")
    private String location;

    @NotBlank(message = "The city is required")
    private String city;

    @NotEmpty(message = "Experience is required")
    private List<CreateExperienceRequest> experiences;

    @NotEmpty(message = "Education is required")
    private List<CreateEducationRequest> education;

    @NotEmpty(message = "Language is required")
    @Valid
    private List<LanguageSelection> languages;

    @NotEmpty(message = "Skills are required")
    private List<SkillSelection> skills;
}
