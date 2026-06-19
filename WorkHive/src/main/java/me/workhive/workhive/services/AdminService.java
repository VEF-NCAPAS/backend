package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.AdminCreateUserRequest;
import me.workhive.workhive.domain.dto.request.AdminCreateCandidateRequest;
import me.workhive.workhive.domain.dto.request.AdminCreateRecruiterRequest;
import me.workhive.workhive.domain.dto.request.AdminUpdateUserRequest;
import me.workhive.workhive.domain.dto.response.ApplicationVolumeReportResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.UserGrowthReportResponse;
import me.workhive.workhive.domain.dto.response.UserResponse;
import me.workhive.workhive.domain.dto.response.VacancyVolumeReportResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;

import java.time.LocalDate;
import java.util.UUID;

public interface AdminService {
    UserResponse createUser(AdminCreateUserRequest request);
    UserResponse createCandidate(AdminCreateCandidateRequest request);
    UserResponse createRecruiter(AdminCreateRecruiterRequest request);
    PageableResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortOrder, Role role, String search);
    PageableResponse<UserResponse> getCandidates(int page, int size, String sortBy, String sortOrder, String search);
    PageableResponse<UserResponse> getRecruiters(int page, int size, String sortBy, String sortOrder, String search);
    UserResponse getUserById(UUID id);
    UserResponse getCandidateById(UUID id);
    UserResponse getRecruiterById(UUID id);
    UserResponse updateUser(UUID id, AdminUpdateUserRequest request);
    UserResponse updateCandidate(UUID id, AdminUpdateUserRequest request);
    UserResponse updateRecruiter(UUID id, AdminUpdateUserRequest request);
    UserResponse deleteUser(UUID id, User currentUser);
    UserResponse deleteCandidate(UUID id, User currentUser);
    UserResponse deleteRecruiter(UUID id, User currentUser);
    VacancyVolumeReportResponse getVacancyVolume(LocalDate from, LocalDate to);
    ApplicationVolumeReportResponse getApplicationVolume(LocalDate from, LocalDate to);
    UserGrowthReportResponse getUserGrowth(LocalDate from, LocalDate to, String groupBy);
}
