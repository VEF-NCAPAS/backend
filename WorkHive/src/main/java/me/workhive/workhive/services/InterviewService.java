package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreateInterviewRequest;
import me.workhive.workhive.domain.dto.request.UpdateInterviewRequest;
import me.workhive.workhive.domain.dto.response.InterviewResponse;
import me.workhive.workhive.domain.entities.User;

import java.util.UUID;

public interface InterviewService {
    InterviewResponse createInterview(CreateInterviewRequest request, User user);
    InterviewResponse updateInterview(UUID id, UpdateInterviewRequest request, User user);
}
