package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.services.RequirementService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/requirement")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService requirementService;
    private final ResponseFactory responseFactory;

    @GetMapping
    @Operation(
            summary = "Obtener todos los requeremientos",
            description = "Reclutador obtiene todos los requerimientos disponibles"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> getAllRequirements() {

        return responseFactory.buildResponse(
                "Requirements retrieved successfully",
                HttpStatus.OK,
                requirementService.getAllRequirements()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener requerimientos por id",
            description = "Reclutador obtiene los requerimientos disponibles por id"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> getRequirementById(
            @PathVariable UUID id
    ) {

        return responseFactory.buildResponse(
                "Requirement retrieved successfully",
                HttpStatus.OK,
                requirementService.getRequirementById(id)
        );
    }
}