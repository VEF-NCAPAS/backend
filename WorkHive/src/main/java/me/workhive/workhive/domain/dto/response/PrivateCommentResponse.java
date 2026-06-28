package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PrivateCommentResponse {

    private UUID id;

    private String description;

    private UUID applicationId;

    private String recruiterName;

    private ApplicationStatus statusAtCreation;

    private LocalDate commentDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}