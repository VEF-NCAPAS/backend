package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateApplicationRequest;
import me.workhive.workhive.domain.dto.request.UpdateApplicationStatusRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.ApplicationService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ResponseFactory responseFactory;

    @PostMapping
    @Operation(
            summary = "Crear postulacion",
            description = "Usuario candidato crea una postulacion"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> createApplication(
            @Valid @RequestBody CreateApplicationRequest request,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Application created successfully",
                HttpStatus.CREATED,
                applicationService.createApplication(request, user)
        );
    }

    @GetMapping
    @Operation(
            summary = "Obtener postulacion",
            description = "Usuarios obtienen todas las postulaciones, si es candidato obtiene unicamente las suyas si es reclutador, las relacionadas a su empresa"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE','RECRUITER')")
    public ResponseEntity<GeneralResponse> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Applications retrieved successfully",
                HttpStatus.OK,
                applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        sortOrder,
                        user
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener postulacion por id",
            description = "Usuarios pueden obtener postulaciones por id"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE','RECRUITER')")
    public ResponseEntity<GeneralResponse> getApplicationById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Application retrieved successfully",
                HttpStatus.OK,
                applicationService.getApplicationById(id, user)
        );
    }

    @GetMapping("/vacancy/{vacancyId}")
    @Operation(
            summary = "Obtener postulacion por id de vacante",
            description = "Usuarios reclutadores pueden obtener las postulaciones relacionadas a una vacante y filtrar por habilidad"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> getApplicationsByVacancy(
            @PathVariable UUID vacancyId,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Applications retrieved successfully",
                HttpStatus.OK,
                applicationService.getApplicationsByVacancy(
                        vacancyId,
                        skill,
                        page,
                        size,
                        user
                )
        );
    }

    @PatchMapping("/{id}/withdraw")
    @Operation(
            summary = "Retirarse de postulacion",
            description = "Usuarios candidatos se pueden retirar de sus postulaciones y ya no seguir con el proceso de seleccion"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> withdrawApplication(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Application withdrawn successfully",
                HttpStatus.OK,
                applicationService.withdrawApplication(id, user)
        );
    }

    @PatchMapping("/{id}/review")
    @Operation(
            summary = "Actualizar estado a revisado",
            description = "Usuarios reclutadores marcan el estado de las postulaciones como revisado"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> reviewApplication(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Application reviewed successfully",
                HttpStatus.OK,
                applicationService.reviewApplication(id, user)
        );
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Actualizar estado SELECCIONADO/RECHAZADO",
            description = "Usuarios reclutadores actualizan el estado de postulacion a seleccionado o a rechazado"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> updateApplicationStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateApplicationStatusRequest request,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Application status updated successfully",
                HttpStatus.OK,
                applicationService.updateApplicationStatus(
                        id,
                        request,
                        user
                )
        );
    }
}