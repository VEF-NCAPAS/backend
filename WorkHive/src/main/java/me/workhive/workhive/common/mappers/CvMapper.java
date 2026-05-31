package me.workhive.workhive.common.mappers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateCvRequest;
import me.workhive.workhive.domain.dto.response.CvResponse;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Cv;
import me.workhive.workhive.domain.entities.Education;
import me.workhive.workhive.domain.entities.Experience;
import me.workhive.workhive.domain.entities.Skill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CvMapper {

    private final ExperienceMapper experienceMapper;
    private final   EducationMapper educationMapper;
    private  final  SkillMapper skillMapper;

    public Cv toCvCreate(CreateCvRequest request, CandidateProfile candidateProfile, List<Experience> experiences, List<Education> educations, List<Skill> skills) {
        return Cv.builder()
                .candidateProfile(candidateProfile)
                .experiences(experiences)
                .education(educations)
                .skills(skills)
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

    public CvResponse toCvDto(Cv cv){
        return CvResponse.builder()
                .id(cv.getId())
                .candidateProfileId(cv.getCandidateProfile().getId())
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
                .skills(
                        cv.getSkills() != null ?
                                cv.getSkills().stream()
                                .map(Skill::getName)
                                .collect(Collectors.toList()) : null
                )
                .build();
    }
}
