package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CandidateScoreRequest;
import me.workhive.workhive.domain.dto.response.CandidateScoreResponse;
import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.Cv;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CandidateMapper {

    public CandidateScoreRequest toSearchRequest(List<String> skills, List<String> languages,
                                                 List<String> education, String location, Integer exp) {
        return CandidateScoreRequest.builder()
                .skills(skills)
                .languages(languages)
                .education(education != null && !education.isEmpty() ? education.get(0) : null)
                .location(location)
                .minimumExperience(exp)
                .build();
    }

    public CandidateScoreResponse toCandidateScoreDto(Application application, double score) {
        Cv cv = application.getCv();
        return CandidateScoreResponse.builder()
                .cvId(cv.getId())
                .applicationId(application.getId())
                .candidateProfileId(cv.getCandidateProfile().getId())
                .name(cv.getCandidateProfile().getUser().getName())
                .email(cv.getCandidateProfile().getUser().getEmail())
                .location(cv.getLocation())
                .city(cv.getCity())
                .score(score)
                .build();
    }
}