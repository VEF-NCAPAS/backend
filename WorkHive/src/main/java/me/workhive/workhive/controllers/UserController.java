package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.services.UserService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ResponseFactory responseFactory;

    @PatchMapping("/change-password")
    @Operation(
            summary = "Cambiar contraseña",
            description = "Usuarios pueden cambiar su contraseña"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        User currentUser = (User) authentication.getPrincipal();

        userService.changePassword(
                currentUser,
                request
        );

        return ResponseEntity.ok("Password updated successfully");
    }

    @GetMapping("/diversity")
    @Operation(
            summary = "Obtener estadísticas globales de diversidad de género",
            description = "Obtiene estadísticas de género acumuladas para Candidatos y Reclutadores"
    )
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getGlobalDiversityStats() {
        return responseFactory.buildResponse(
                "Global gender diversity statistics retrieved successfully",
                HttpStatus.OK,
                userService.getGlobalGenderDiversityStats()
        );
    }
}
