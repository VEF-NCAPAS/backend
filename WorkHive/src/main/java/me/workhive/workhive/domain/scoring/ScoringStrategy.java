package me.workhive.workhive.domain.scoring;


import me.workhive.workhive.domain.dto.request.CandidateSearchRequest;
import me.workhive.workhive.domain.entities.Cv;

public interface ScoringStrategy {

   int calculate(Cv cv, CandidateSearchRequest request);

   int getMaxScore(CandidateSearchRequest request);

}