package me.workhive.workhive.domain.scoring;

import me.workhive.workhive.domain.dto.request.CandidateScoreRequest;
import me.workhive.workhive.domain.entities.Cv;
import org.springframework.stereotype.Component;

@Component
public class SkillScoringStrategy implements ScoringStrategy {

    private static final int MAX_SCORE = 40;

    @Override
    public int calculate(Cv cv, CandidateScoreRequest request) {

        if (request.getSkills() == null ||
                request.getSkills().isEmpty()) {
            return 0;
        }

        long matchedSkills =
                request.getSkills()
                        .stream()
                        .filter(requiredSkill ->
                                cv.getSkills()
                                        .stream()
                                        .anyMatch(skill ->
                                                skill.getName()
                                                        .equalsIgnoreCase(requiredSkill)
                                        )
                        )
                        .count();

        return (int) ((matchedSkills * MAX_SCORE) / request.getSkills().size());
    }

    @Override
    public int getMaxScore(CandidateScoreRequest request) {

        return request.getSkills() == null ||
                request.getSkills().isEmpty()
                ? 0
                : MAX_SCORE;
    }
}