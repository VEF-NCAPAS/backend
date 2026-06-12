package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.request.UpdatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.PrivateCommentResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;

import java.util.UUID;

public interface PrivateCommentService {

    PrivateCommentResponse createPrivateComment(CreatePrivateCommentRequest request, User user);
    PageableResponse<PrivateCommentResponse> getCommentsByApplication(
            UUID applicationId,
            int page,
            int size,
            String sortBy,
            String sortOrder,
            ApplicationStatus status
    );

    PrivateCommentResponse getCommentById(UUID id, User user);

    PrivateCommentResponse updateComment(
            UUID id,
            UpdatePrivateCommentRequest request,
            User user
    );

    PrivateCommentResponse deleteComment(
            UUID id,
            User user
    );

}
