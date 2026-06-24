package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.response.CandidateScoreResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface CandidateService {
    PageableResponse<CandidateScoreResponse> getAllCandidatesByVacancy(
            UUID vacancyId,
            List<String> skills,
            List<String> languages,
            List<String> education,
            String location,
            Integer minimumExperience,
            int page,
            int size,
            String sortOrder,
            User user
    );
}