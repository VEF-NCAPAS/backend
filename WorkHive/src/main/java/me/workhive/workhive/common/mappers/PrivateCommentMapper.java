package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.response.PrivateCommentResponse;
import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.PrivateComment;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PrivateCommentMapper {
    public PrivateComment toPrivateCommentCreate(
            CreatePrivateCommentRequest request,
            RecruiterProfile recruiter,
            Application application
    ) {

        return PrivateComment.builder()
                .description(request.getDescription())
                .recruiter(recruiter)
                .application(application)
                .statusAtCreation(
                        application.getApplicationStatus()
                )
                .build();
    }

    public PrivateCommentResponse toPrivateCommentDto(
            PrivateComment comment
    ) {

        return PrivateCommentResponse.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .applicationId(
                        comment.getApplication().getId()
                )
                .recruiterName(
                        comment.getRecruiter()
                                .getUser()
                                .getName()
                )
                .statusAtCreation(
                        comment.getStatusAtCreation()
                )
                .commentDate(
                        comment.getCommentDate()
                )
                .createdAt(
                        comment.getCreatedAt()
                )
                .updatedAt(
                        comment.getUpdatedAt()
                )
                .build();
    }

    public Page<PrivateCommentResponse> toDtoPrivateCommentList(
            Page<PrivateComment> comments
    ) {
        return comments.map(
                this::toPrivateCommentDto
        );
    }
}