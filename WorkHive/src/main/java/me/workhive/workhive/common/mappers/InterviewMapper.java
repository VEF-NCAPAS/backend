package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreateInterviewRequest;
import me.workhive.workhive.domain.dto.request.UpdateInterviewRequest;
import me.workhive.workhive.domain.dto.response.InterviewResponse;
import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.Interview;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InterviewMapper {

    public Interview toInterviewCreate(CreateInterviewRequest request, Application application) {

        return Interview.builder()
                .application(application)
                .interviewDate(request.getInterviewDate())
                .meetingLink(request.getMeetingLink())
                .build();
    }

    public Interview toInterviewUpdate(UpdateInterviewRequest request, UUID id, Interview existingInterview) {
        return Interview.builder()
                .id(id)
                .application(existingInterview.getApplication())
                .createdAt(existingInterview.getCreatedAt())
                .interviewDate(request.getInterviewDate())
                .meetingLink(request.getMeetingLink())
                .build();
    }

    public InterviewResponse toInterviewDto(Interview interview) {
        if (interview == null) return null;

        Application app = interview.getApplication();
        return InterviewResponse.builder()
                .id(interview.getId())
                .applicationId(app != null ? app.getId() : null)
                .candidateName(
                        (app != null && app.getCandidate() != null && app.getCandidate().getUser() != null)
                                ? app.getCandidate().getUser().getName() : "Not Known"
                )
                .vacancyTitle(app != null && app.getVacancy() != null ? app.getVacancy().getTitle() : "N/A")
                .interviewDate(interview.getInterviewDate())
                .meetingLink(interview.getMeetingLink())
                .build();
    }

    public Page<InterviewResponse> toDtoInterviewList(Page<Interview> interviews) {

        return interviews.map(this::toInterviewDto);
    }
}