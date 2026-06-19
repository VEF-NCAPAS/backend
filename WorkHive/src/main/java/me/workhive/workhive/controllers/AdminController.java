package me.workhive.workhive.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.AdminCreateUserRequest;
import me.workhive.workhive.domain.dto.request.AdminUpdateUserRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.services.AdminService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminController {

    private final AdminService adminService;
    private final ResponseFactory responseFactory;

    @PostMapping("/users")
    public ResponseEntity<GeneralResponse> createUser(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {
        return responseFactory.buildResponse(
                "User created successfully",
                HttpStatus.CREATED,
                adminService.createUser(request)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<GeneralResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String search
    ) {
        return responseFactory.buildResponse(
                "Users retrieved successfully",
                HttpStatus.OK,
                adminService.getAllUsers(page, size, sortBy, sortOrder, role, search)
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<GeneralResponse> getUserById(
            @PathVariable UUID id
    ) {
        return responseFactory.buildResponse(
                "User retrieved successfully",
                HttpStatus.OK,
                adminService.getUserById(id)
        );
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<GeneralResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {
        return responseFactory.buildResponse(
                "User updated successfully",
                HttpStatus.OK,
                adminService.updateUser(id, request)
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<GeneralResponse> deleteUser(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return responseFactory.buildResponse(
                "User deleted successfully",
                HttpStatus.OK,
                adminService.deleteUser(id, currentUser)
        );
    }

    @GetMapping("/reports/vacancies")
    public ResponseEntity<GeneralResponse> getVacancyVolume(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return responseFactory.buildResponse(
                "Vacancy volume report retrieved successfully",
                HttpStatus.OK,
                adminService.getVacancyVolume(from, to)
        );
    }

    @GetMapping("/reports/applications")
    public ResponseEntity<GeneralResponse> getApplicationVolume(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return responseFactory.buildResponse(
                "Application volume report retrieved successfully",
                HttpStatus.OK,
                adminService.getApplicationVolume(from, to)
        );
    }

    @GetMapping("/reports/users-growth")
    public ResponseEntity<GeneralResponse> getUserGrowth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "day") String groupBy
    ) {
        return responseFactory.buildResponse(
                "User growth report retrieved successfully",
                HttpStatus.OK,
                adminService.getUserGrowth(from, to, groupBy)
        );
    }
}