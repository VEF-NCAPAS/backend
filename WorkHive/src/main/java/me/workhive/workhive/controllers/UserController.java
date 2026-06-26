package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.*;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.services.AuthService;
import me.workhive.workhive.services.UserService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ResponseFactory responseFactory;
    private final AuthService authService;

    @Operation(
            summary = "Registrar candidato",
            description = "Permite registrar un nuevo usuario con rol de candidato"
    )
    @PostMapping("/register/candidate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> registerCandidate(@Valid @RequestBody CandidateRegisterRequest registerRequest){
        return responseFactory.buildResponse(
                "Candidate registered",
                HttpStatus.CREATED,
                authService.registerCandidate(registerRequest)
        );
    }

    @Operation(
            summary = "Registrar reclutador",
            description = "Permite registrar un nuevo usuario con rol de reclutador"
    )
    @PostMapping("/register/recruiter")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest registerRequest){
        return responseFactory.buildResponse(
                "Recruiter registered",
                HttpStatus.CREATED,
                authService.registerRecruiter(registerRequest)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getUsers(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {

        return responseFactory.buildResponse(
                "Users retrieved successfully",
                HttpStatus.OK,
                userService.getUsers(page, size, sortBy, sortOrder)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener usuario por id",
            description = "Obtiene usuarios segun su id"
    )
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getUserById(
            @PathVariable UUID id
    ) {

        return responseFactory.buildResponse(
                "User retrieved successfully",
                HttpStatus.OK,
                userService.getUserById(id)
        );
    }

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

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        User user = (User) authentication.getPrincipal();

        return responseFactory.buildResponse(
                "Profile updated successfully",
                HttpStatus.OK,
                userService.updateMyProfile(user, request)
        );
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        return responseFactory.buildResponse(
                "User updated successfully",
                HttpStatus.OK,
                userService.adminUpdateUser(id, request)
        );
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina usuario segun id"
    )
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> deleteUser(
            @PathVariable UUID id
    ) {

        return responseFactory.buildResponse(
                "User deleted successfully",
                HttpStatus.OK,
                userService.deleteUser(id)
        );
    }
}
