package me.workhive.workhive.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.request.UpdatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import me.workhive.workhive.services.PrivateCommentService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/privateComment")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;
    private final ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> createComment(
            @PathVariable UUID applicationId,
            @Valid @RequestBody CreatePrivateCommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Private comment created successfully",
                HttpStatus.CREATED,
                privateCommentService.createPrivateComment(request,applicationId,user)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> getCommentById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Private comment retrieved successfully",
                HttpStatus.OK,
                privateCommentService.getCommentById(id, user)
        );
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<GeneralResponse> getCommentsByApplication(
            @PathVariable UUID applicationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) ApplicationStatus status
    ) {
        return responseFactory.buildResponse(
                "Comments retrieved succesfully",
                HttpStatus.OK,
                privateCommentService.getCommentsByApplication(
                        applicationId,
                        page,
                        size,
                        sortBy,
                        sortOrder,
                        status
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> updateComment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePrivateCommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Private comment description updated successfully",
                HttpStatus.OK,
                privateCommentService.updateComment(
                        id,
                        request,
                        user
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> deleteComment(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Private comment deleted successfully",
                HttpStatus.OK,
                privateCommentService.deleteComment(
                        id,
                        user
                )
        );
    }
}