package me.workhive.workhive.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CandidateScoreRequest {

    private List<String> skills;

    private List<String> languages;

    private String education;

    private Integer minimumExperience;

    private String location;
}