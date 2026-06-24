package me.workhive.workhive.domain.scoring;

import me.workhive.workhive.domain.dto.request.CandidateSearchRequest;
import me.workhive.workhive.domain.entities.Cv;
import me.workhive.workhive.utils.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EducationScoringStrategy implements ScoringStrategy {

    private static final int MAX_SCORE = 15;
    @Override
    public int calculate(Cv cv, CandidateSearchRequest request) {

        if (request.getEducation() == null || request.getEducation().isBlank()) {
            return 0;
        }
        String searchEducation = StringUtils.normalize(request.getEducation());

        boolean matches = cv.getEducation().stream()
                .anyMatch(edu -> {
                    String cvMajor = StringUtils.normalize(edu.getMajor());
                    return cvMajor.equals(searchEducation);
                });

        return matches ? MAX_SCORE : 0;
    }

    @Override
    public int getMaxScore(CandidateSearchRequest request) {

        return request.getEducation() == null ||
                request.getEducation().isBlank()
                ? 0
                : MAX_SCORE;
    }
}