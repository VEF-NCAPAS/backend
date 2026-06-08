package me.workhive.workhive.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.request.UpdateTechnicalTestRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.TechnicalTestService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/technical-tests")
@RequiredArgsConstructor
public class TechnicalTestController {

    private final TechnicalTestService technicalTestService;
    private final ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> createTechnicalTest(
            @Valid @RequestBody CreateTechnicalTestRequest request,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Technical test created successfully",
                HttpStatus.CREATED,
                technicalTestService.createTechnicalTest(request, user)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> updateTechnicalTest(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTechnicalTestRequest request,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Technical test updated successfully",
                HttpStatus.OK,
                technicalTestService.updateTechnicalTest(id, request, user)
        );
    }
}