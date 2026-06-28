package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateCvRequest;
import me.workhive.workhive.domain.dto.request.UpdateCvRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.impl.CvServiceImpl;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
public class CvController {
    private final CvServiceImpl cvService;

    private final ResponseFactory responseFactory;

    @PostMapping
    @Operation(
            summary = "Crear cv",
            description = "Permite al usuario crear su CV"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> createCv(@Valid @RequestBody CreateCvRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId =  user.getId();

        return responseFactory.buildResponse(
                "CV created successfully",
                HttpStatus.CREATED,
                cvService.createCv(userId, request)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener cv por id",
            description = "Permite obtener el CV por id"
    )
    @PreAuthorize("hasAnyRole( 'RECRUITER', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getCvById(
            @PathVariable UUID id
    ) {

        return responseFactory.buildResponse(
                "Cv retrieved successfully",
                HttpStatus.OK,
                cvService.getCvById(id)
        );
    }
    @GetMapping("/me")
    @Operation(
            summary = "Obtener mi cv",
            description = "Permite al usuario obtener su CV"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> getMyCv(
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "CV retrieved successfully",
                HttpStatus.OK,
                cvService.getCvByCandidate(user)
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar cv",
            description = "Permite al usuario actualizar su CV"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> updateCv(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCvRequest request,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Cv updated successfully",
                HttpStatus.OK,
                cvService.updateCv(id, request, user)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Borrar cv",
            description = "Permite al usuario eliminar su CV"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> deleteCv(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {

        return responseFactory.buildResponse(
                "Cv deleted successfully",
                HttpStatus.OK,
                cvService.deleteCv(id, user)
        );
    }

}