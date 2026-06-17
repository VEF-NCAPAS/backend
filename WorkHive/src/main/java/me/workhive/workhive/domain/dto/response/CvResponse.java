package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CvResponse {

    private UUID id;

    private UUID candidateProfileId;

    private String name;

    private String email;

    private List<ExperienceResponse> experiences;

    private List<EducationResponse> education;

    private List<String> skills;
}
