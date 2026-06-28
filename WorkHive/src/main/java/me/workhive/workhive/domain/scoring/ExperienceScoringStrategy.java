package me.workhive.workhive.domain.scoring;

import me.workhive.workhive.domain.dto.request.CandidateScoreRequest;
import me.workhive.workhive.domain.entities.Cv;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
@Component
public class ExperienceScoringStrategy implements ScoringStrategy {

    private static final int MAX_SCORE = 20;

    @Override
    public int calculate(Cv cv, CandidateScoreRequest request) {

        if (request.getMinimumExperience() == null) {
            return 0;
        }

        long years = cv.getExperiences()
                .stream()
                .mapToLong(exp ->
                        ChronoUnit.YEARS.between(
                                exp.getHireDate(),
                                exp.getEndDate()
                        )
                )
                .sum();

        if (years >= request.getMinimumExperience()) {
            return MAX_SCORE;
        }

        return (int) (((double) years / request.getMinimumExperience()) * MAX_SCORE);
    }

    @Override
    public int getMaxScore(CandidateScoreRequest request) {

        return request.getMinimumExperience() != null
                ? MAX_SCORE
                : 0;
    }
}