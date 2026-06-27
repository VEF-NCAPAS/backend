package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.request.UpdateUserRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.AuthService;
import me.workhive.workhive.services.UserService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/register/candidate")
    @Operation(
            summary = "Registrar candidato",
            description = "Permite registrar un nuevo usuario con rol de candidato"
    )
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> registerCandidate(@Valid @RequestBody CandidateRegisterRequest registerRequest){
        return responseFactory.buildResponse(
                "Candidate registered",
                HttpStatus.CREATED,
                authService.registerCandidate(registerRequest)
        );
    }

    @PostMapping("/register/recruiter")
    @Operation(
            summary = "Registrar reclutador",
            description = "Permite registrar un nuevo usuario con rol de reclutador"
    )
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest registerRequest){
        return responseFactory.buildResponse(
                "Recruiter registered",
                HttpStatus.CREATED,
                authService.registerRecruiter(registerRequest)
        );
    }

    @GetMapping("/all")
    @Operation(
            summary = "Obtener a todos los usuarios",
            description = "Administrador obtiene a todos los usuarios"
    )
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

    @GetMapping("/reports/users-growth")
    @Operation(
            summary = "Obtener el reporte de crecimiento de usuario",
            description = "Obtiene los reportes de crecimiento de usuario"
    )
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getUserGrowth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "day") String groupBy
    ) {
        return responseFactory.buildResponse(
                "User growth report retrieved successfully",
                HttpStatus.OK,
                userService.getUserGrowth(from, to, groupBy)
        );
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
    @Operation(
            summary = "actualizar perfil",
            description = "Candidato actualiza datos de su perfil"
    )
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
    @Operation(
            summary = "Actualizar usuario",
            description = "Administrador actualiza a un usuario por id"
    )
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
            description = "Administrador elimina usuario segun id"
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
