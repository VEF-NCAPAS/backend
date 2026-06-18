package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
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
}
