package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateInterviewRequest;
import me.workhive.workhive.domain.dto.request.UpdateInterviewRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.InterviewService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;
    private final ResponseFactory responseFactory;

    @PostMapping
    @Operation(
                summary = "Crear entrevista",
            description = "Usuario reclutador crea entrevistas"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> createInterview(
            @Valid @RequestBody CreateInterviewRequest request,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Interview scheduled successfully",
                HttpStatus.CREATED,
                interviewService.createInterview(request, user)
        );
    }

    @Operation(
            summary = "Actualizar entrevista",
            description = "Se pueden actualizar cualquier dato de la entrevista"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> updateInterview(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInterviewRequest request,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Interview updated successfully",
                HttpStatus.OK,
                interviewService.updateInterview(id, request, user)
        );
    }
}