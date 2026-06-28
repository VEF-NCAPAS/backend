package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateInterviewRequest {

    @NotNull(message = "Interview date is required")
    @Future(message = "Interview date must be in the future")
    private LocalDateTime interviewDate;

    @NotBlank(message = "Meeting link is required")
    private String meetingLink;
}