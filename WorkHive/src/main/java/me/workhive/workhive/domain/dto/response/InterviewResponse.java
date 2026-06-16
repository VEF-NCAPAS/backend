package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InterviewResponse {

    private UUID id;
    private UUID applicationId;
    private String candidateName;
    private String vacancyTitle;
    private LocalDateTime interviewDate;
    private String meetingLink;
}