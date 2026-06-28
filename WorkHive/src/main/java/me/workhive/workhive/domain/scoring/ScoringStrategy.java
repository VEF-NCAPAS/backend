package me.workhive.workhive.domain.scoring;


import me.workhive.workhive.domain.dto.request.CandidateScoreRequest;
import me.workhive.workhive.domain.entities.Cv;

public interface ScoringStrategy {

   int calculate(Cv cv, CandidateScoreRequest request);

   int getMaxScore(CandidateScoreRequest request);

}