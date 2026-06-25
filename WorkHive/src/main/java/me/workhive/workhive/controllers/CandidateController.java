package me.workhive.workhive.controllers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.response.CandidateScoreResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/vacancy/{vacancyId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<PageableResponse<CandidateScoreResponse>> getAllCandidatesByVacancy(
            @PathVariable UUID vacancyId,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) List<String> languages,
            @RequestParam(required = false) List<String> education,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minimumExperience,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @AuthenticationPrincipal User user

    ) {
        return ResponseEntity.ok(
                candidateService.getAllCandidatesByVacancy(
                        vacancyId,
                        skills,
                        languages,
                        education,
                        location,
                        minimumExperience,
                        page,
                        size,
                        sortOrder,
                        user
                )
        );
    }
}