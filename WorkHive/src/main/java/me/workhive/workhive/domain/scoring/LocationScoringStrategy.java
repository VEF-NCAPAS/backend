package me.workhive.workhive.domain.scoring;

import me.workhive.workhive.domain.dto.request.CandidateScoreRequest;
import me.workhive.workhive.domain.entities.Cv;
import me.workhive.workhive.utils.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class LocationScoringStrategy implements ScoringStrategy {

    private static final int MAX_SCORE = 10;

    @Override
    public int calculate(Cv cv, CandidateScoreRequest request) {

        if (request.getLocation() == null ||
                request.getLocation().isBlank()) {
            return 0;
        }
        if (cv.getCity() == null) {
            return 0;
        }

        String cvCity = StringUtils.normalize(cv.getCity());
        String requestCity = StringUtils.normalize(request.getLocation());

        return cvCity.equals(requestCity) ? MAX_SCORE : 0;
    }

    @Override
    public int getMaxScore(CandidateScoreRequest request) {

        return request.getLocation() == null ||
                request.getLocation().isBlank()
                ? 0
                : MAX_SCORE;
    }
}