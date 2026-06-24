package me.workhive.workhive.domain.scoring;

import me.workhive.workhive.domain.dto.request.CandidateSearchRequest;
import me.workhive.workhive.domain.entities.Cv;
import me.workhive.workhive.utils.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class LanguageScoringStrategy implements ScoringStrategy {

    private static final int MAX_SCORE = 15;
    @Override
    public int calculate(Cv cv, CandidateSearchRequest request) {

        if (request.getLanguages() == null ||
                request.getLanguages().isEmpty()) {
            return 0;
        }

        long matches = request.getLanguages().stream()
                .filter(reqLang ->
                        cv.getCvLanguages().stream()
                                .anyMatch(cvLang ->
                                        StringUtils.normalize(cvLang.getLanguage().getName())
                                                .equals(StringUtils.normalize(reqLang))
                                )
                )
                .count();

        return (int)
                ((matches * MAX_SCORE) / request.getLanguages().size());
    }

    @Override
    public int getMaxScore(CandidateSearchRequest request) {

        return request.getLanguages() == null ||
                request.getLanguages().isEmpty()
                ? 0
                : MAX_SCORE;
    }
}