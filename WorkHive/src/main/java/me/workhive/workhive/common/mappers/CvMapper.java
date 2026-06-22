package me.workhive.workhive.common.mappers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateCvRequest;
import me.workhive.workhive.domain.dto.request.UpdateCvRequest;
import me.workhive.workhive.domain.dto.response.CvResponse;
import me.workhive.workhive.domain.entities.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CvMapper {

    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;
    private final CvLanguageMapper cvLanguageMapper;

    public Cv toCvCreate(CreateCvRequest request, CandidateProfile candidateProfile, List<Experience> experiences, List<Education> educations, List<CvLanguage> languages, List<Skill> skills) {
        return Cv.builder()
                .candidateProfile(candidateProfile)
                .professionalSummary(request.getProfessionalSummary())
                .location(request.getLocation())
                .city(request.getCity())
                .experiences(experiences)
                .education(educations)
                .cvLanguages(languages)
                .skills(skills)
                .build();
    }

    public void toCvUpdate(Cv cv, UpdateCvRequest request,  List<CvLanguage> languages, List<Education> education, List<Skill> skills, List<Experience> experiences){
        cv.setProfessionalSummary(request.getProfessionalSummary());
        cv.setLocation(request.getLocation());
        cv.setCity(request.getCity());

        cv.getExperiences().clear();
        cv.getExperiences().addAll(experiences);

        cv.getEducation().clear();
        cv.getEducation().addAll(education);

        cv.getCvLanguages().clear();
        cv.getCvLanguages().addAll(languages);

        cv.setSkills(skills);
    }

    public CvResponse toCvDto(Cv cv){
        return CvResponse.builder()
                .id(cv.getId())
                .name(cv.getCandidateProfile().getUser().getName())
                .email(cv.getCandidateProfile().getUser().getEmail())
                .candidateProfileId(cv.getCandidateProfile().getId())
                .professionalSummary(cv.getProfessionalSummary())
                .location(cv.getLocation())
                .city(cv.getCity())
                .experiences(
                        cv.getExperiences() != null ?
                                cv.getExperiences().stream()
                                .map(experienceMapper::toDto)
                                .collect(Collectors.toList()) : null
                )
                .education(
                        cv.getEducation() != null ?
                                cv.getEducation().stream()
                                .map(educationMapper::toDto)
                                .collect(Collectors.toList()) : null
                )
                .languages(
                        cv.getCvLanguages() != null ?
                                cv.getCvLanguages().stream()
                                .map(cvLanguageMapper::toDto)
                                .collect(Collectors.toList()) : null
                )
                .skills(
                        cv.getSkills() != null ?
                                cv.getSkills().stream()
                                .map(Skill::getName)
                                .collect(Collectors.toList()) : null
                )
                .build();
    }
}
