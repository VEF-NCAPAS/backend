package me.workhive.workhive.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.AdminCreateCandidateRequest;
import me.workhive.workhive.domain.dto.request.AdminCreateRecruiterRequest;
import me.workhive.workhive.domain.dto.request.AdminUpdateUserRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.AdminService;
import me.workhive.workhive.utils.ResponseFactory;
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

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminUserController {
    private final AdminService adminService;
    private final ResponseFactory responseFactory;

    @PostMapping("/candidate")
    public ResponseEntity<GeneralResponse> createCandidate(
            @Valid @RequestBody AdminCreateCandidateRequest request
    ) {
        return responseFactory.buildResponse(
                "Candidate user created successfully",
                HttpStatus.CREATED,
                adminService.createCandidate(request)
        );
    }

    @GetMapping("/candidate")
    public ResponseEntity<GeneralResponse> getCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String search
    ) {
        return responseFactory.buildResponse(
                "Candidate users retrieved successfully",
                HttpStatus.OK,
                adminService.getCandidates(page, size, sortBy, sortOrder, search)
        );
    }

    @GetMapping("/candidate/{id}")
    public ResponseEntity<GeneralResponse> getCandidateById(@PathVariable UUID id) {
        return responseFactory.buildResponse(
                "Candidate user retrieved successfully",
                HttpStatus.OK,
                adminService.getCandidateById(id)
        );
    }

    @PatchMapping("/candidate/{id}")
    public ResponseEntity<GeneralResponse> updateCandidate(
            @PathVariable UUID id,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {
        return responseFactory.buildResponse(
                "Candidate user updated successfully",
                HttpStatus.OK,
                adminService.updateCandidate(id, request)
        );
    }

    @DeleteMapping("/candidate/{id}")
    public ResponseEntity<GeneralResponse> deleteCandidate(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return responseFactory.buildResponse(
                "Candidate user deleted successfully",
                HttpStatus.OK,
                adminService.deleteCandidate(id, currentUser)
        );
    }

    @PostMapping("/recruiter")
    public ResponseEntity<GeneralResponse> createRecruiter(
            @Valid @RequestBody AdminCreateRecruiterRequest request
    ) {
        return responseFactory.buildResponse(
                "Recruiter user created successfully",
                HttpStatus.CREATED,
                adminService.createRecruiter(request)
        );
    }

    @GetMapping("/recruiter")
    public ResponseEntity<GeneralResponse> getRecruiters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String search
    ) {
        return responseFactory.buildResponse(
                "Recruiter users retrieved successfully",
                HttpStatus.OK,
                adminService.getRecruiters(page, size, sortBy, sortOrder, search)
        );
    }

    @GetMapping("/recruiter/{id}")
    public ResponseEntity<GeneralResponse> getRecruiterById(@PathVariable UUID id) {
        return responseFactory.buildResponse(
                "Recruiter user retrieved successfully",
                HttpStatus.OK,
                adminService.getRecruiterById(id)
        );
    }

    @PatchMapping("/recruiter/{id}")
    public ResponseEntity<GeneralResponse> updateRecruiter(
            @PathVariable UUID id,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {
        return responseFactory.buildResponse(
                "Recruiter user updated successfully",
                HttpStatus.OK,
                adminService.updateRecruiter(id, request)
        );
    }

    @DeleteMapping("/recruiter/{id}")
    public ResponseEntity<GeneralResponse> deleteRecruiter(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return responseFactory.buildResponse(
                "Recruiter user deleted successfully",
                HttpStatus.OK,
                adminService.deleteRecruiter(id, currentUser)
        );
    }
}
