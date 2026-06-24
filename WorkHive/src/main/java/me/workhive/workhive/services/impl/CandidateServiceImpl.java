package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.CandidateMapper;
import me.workhive.workhive.domain.dto.request.CandidateScoreRequest;
import me.workhive.workhive.domain.dto.response.CandidateScoreResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.*;
import me.workhive.workhive.domain.scoring.ScoringStrategy;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.ApplicationRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.VacancyRepository;
import me.workhive.workhive.services.CandidateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateMapper candidateMapper;
    private final List<ScoringStrategy> scoringStrategies;
    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final RecruiterRepository recruiterRepository;


    @Override
    public PageableResponse<CandidateScoreResponse> getAllCandidatesByVacancy(
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
    ) {
        RecruiterProfile recruiterProfile = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy not found"));

        if (!vacancy.getCompany().getId().equals(recruiterProfile.getCompany().getId())) {
            throw new DeniedAccessException("You cannot view this vacancy");
        }
        Page<Application> applicationPage = applicationRepository.findByVacancy_Id(vacancyId, PageRequest.of(page, size));

        CandidateScoreRequest request = candidateMapper.toSearchRequest(
                skills, languages, education, location, minimumExperience
        );

        Page<CandidateScoreResponse> responsePage = applicationPage.map(app -> {
            Cv cv = app.getCv();
            double score = calculateScore(cv, request);
            return candidateMapper.toCandidateScoreDto(cv, score);
        });

        List<CandidateScoreResponse> sortedContent = responsePage.getContent().stream()
                .sorted(sortOrder.equalsIgnoreCase("desc")
                        ? Comparator.comparingDouble(CandidateScoreResponse::getScore).reversed()
                        : Comparator.comparingDouble(CandidateScoreResponse::getScore))
                .toList();

        return PageableResponse.<CandidateScoreResponse>builder()
                .content(sortedContent)
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .totalElements(responsePage.getTotalElements())
                .totalPages(responsePage.getTotalPages())
                .last(responsePage.isLast())
                .build();
    }

    private double calculateScore(Cv cv, CandidateScoreRequest request) {
        int earnedScore = 0;
        int maxScore = 0;

        for (ScoringStrategy strategy : scoringStrategies) {
            earnedScore += strategy.calculate(cv, request);
            maxScore += strategy.getMaxScore(request);
        }

        return (maxScore == 0) ? 0.0 : ((double) earnedScore / maxScore) * 100;
    }
}