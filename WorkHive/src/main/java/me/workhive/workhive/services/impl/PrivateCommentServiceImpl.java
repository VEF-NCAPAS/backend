package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.PrivateCommentMapper;
import me.workhive.workhive.domain.dto.request.CreatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.request.UpdatePrivateCommentRequest;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.PrivateCommentResponse;
import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.PrivateComment;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.ApplicationRepository;
import me.workhive.workhive.repositories.PrivateCommentRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.services.PrivateCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final PrivateCommentRepository privateCommentRepository;

    private final ApplicationRepository applicationRepository;

    private final RecruiterRepository recruiterRepository;

    private final PrivateCommentMapper privateCommentMapper;


    @Override
    @Transactional
    public PrivateCommentResponse createPrivateComment(CreatePrivateCommentRequest request, UUID applicationId, User user) {
        RecruiterProfile recruiter = findRecruiter(user);

        Application application = findApplication(applicationId);

        if (!application.getVacancy()
                .getCompany()
                .getId()
                .equals(recruiter.getCompany().getId())) {

            throw new DeniedAccessException(
                    "You cannot comment applications from another company"
            );
        }

        PrivateComment comment =
                privateCommentMapper.toPrivateCommentCreate(
                        request,
                        recruiter,
                        application
                );

        comment.setStatusAtCreation(
                application.getApplicationStatus()
        );

        PrivateComment savedComment =
                privateCommentRepository.save(comment);

        return privateCommentMapper.toPrivateCommentDto(savedComment);
    }

    @Override
    public PageableResponse<PrivateCommentResponse> getCommentsByApplication(UUID applicationId, int page, int size, String sortBy, String sortOrder, ApplicationStatus status) {
        Application application = findApplication(applicationId);

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<PrivateComment> comments;

        if (status != null) {

            comments = privateCommentRepository
                    .findByApplicationAndStatusAtCreation(
                            application,
                            status,
                            pageable
                    );

        } else {

            comments = privateCommentRepository
                    .findByApplication(
                            application,
                            pageable
                    );
        }

        if (comments.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No comments found"
            );
        }

        Page<PrivateCommentResponse> commentPage =
                privateCommentMapper.toDtoPrivateCommentList(comments);

        return PageableResponse.<PrivateCommentResponse>builder()
                .content(commentPage.getContent())
                .page(commentPage.getNumber())
                .size(commentPage.getSize())
                .totalElements(commentPage.getTotalElements())
                .last(commentPage.isLast())
                .build();
    }

    @Override
    public PrivateCommentResponse getCommentById(UUID id, User user) {
        PrivateComment privateComment = findPrivateComment(id);

        if(user.getRole() == Role.RECRUITER){
            RecruiterProfile recruiter = findRecruiter(user);
            if (!privateComment.getRecruiter().getCompany().getId().equals(recruiter.getCompany().getId())){
                throw new DeniedAccessException("You cannot view private comments from another company");
            }
            return privateCommentMapper.toPrivateCommentDto(privateComment);
        }
        throw new DeniedAccessException("Role not authorized");
    }

    @Override
    public PrivateCommentResponse updateComment(UUID id, UpdatePrivateCommentRequest request, User user) {
        PrivateComment existingComment = findPrivateComment(id);

        RecruiterProfile recruiter = findRecruiter(user);

        if (!existingComment.getRecruiter().getCompany().getId().equals(recruiter.getCompany().getId())) {

            throw new DeniedAccessException(
                    "You cannot edit comments from another company"
            );
        }

        existingComment.setDescription(request.getDescription());

        PrivateComment savedComment = privateCommentRepository.save(existingComment);

        return privateCommentMapper.toPrivateCommentDto(savedComment);
    }

    @Override
    public PrivateCommentResponse deleteComment(UUID id, User user) {
        PrivateComment comment = findPrivateComment(id);

        RecruiterProfile recruiter = findRecruiter(user);

        if (!comment.getRecruiter().getCompany().getId().equals(recruiter.getCompany().getId())) {
            throw new DeniedAccessException(
                    "You cannot delete comments from another company"
            );
        }

        PrivateCommentResponse response = privateCommentMapper.toPrivateCommentDto(comment);

        privateCommentRepository.delete(comment);

        return response;
    }

    private RecruiterProfile findRecruiter(User user) {
        return recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
    }

    private Application findApplication(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private PrivateComment findPrivateComment(UUID id){
        return privateCommentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Private Comment not found"));
    }
}
