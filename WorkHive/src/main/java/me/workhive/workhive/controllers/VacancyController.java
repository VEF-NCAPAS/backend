package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateVacancyRequest;
import me.workhive.workhive.domain.dto.request.UpdateVacancyRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Modality;
import me.workhive.workhive.services.VacancyService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vacancy")
@RequiredArgsConstructor
public class VacancyController {
    public final VacancyService vacancyService;
    public final ResponseFactory responseFactory;

    @PostMapping
    @Operation(
            summary = "Crear vacante",
            description = "Usuario reclutador vacante de empleo"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> createVacancy(@Valid @RequestBody CreateVacancyRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return responseFactory.buildResponse(
                "Vacancy created successfully",
                HttpStatus.CREATED,
                vacancyService.createVacancy(request, user)
        );
    }

    @GetMapping
    @Operation(
            summary = "Obtener vacantes",
            description = "Obtiene todas las vacantes y se puede filtrar"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getAllVacancies(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Modality modality,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Vacancies retrieved successfully",
                HttpStatus.OK,
                vacancyService.getAllVacancies(page, size, sortBy, sortOrder,
                        title, modality, user)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener vacante por id",
            description = "Obtiene vacantes segun su id"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getVacancyById(
            @PathVariable UUID id
    ) {

        return responseFactory.buildResponse(
                "Vacancy retrieved successfully",
                HttpStatus.OK,
                vacancyService.getVacancyById(id)
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar vacante",
            description = "Permite actualizar vacantes segun su id"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> updateVacancy(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVacancyRequest request,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Vacancy updated successfully",
                HttpStatus.OK,
                vacancyService.updateVacancy(id, request, user)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar vacante",
            description = "Elimina vacante segun id"
    )
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> deleteVacancy(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Vacancy deleted successfully",
                HttpStatus.OK,
                vacancyService.deleteVacancy(id, user)
        );
    }
}
