package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.*;
import me.workhive.workhive.domain.dto.response.*;
import me.workhive.workhive.domain.entities.User;

import java.util.Map;

import java.time.LocalDate;
import java.util.UUID;

public interface UserService {
    void changePassword(User user, ChangePasswordRequest request);

    Map<String, Long> getGlobalGenderDiversityStats();
    UserResponse getMyProfile(User currentUser);
    UserResponse updateMyProfile(User currentUser, UpdateUserRequest request);
    UserResponse adminUpdateUser(UUID userId, UpdateUserRequest request);
    UserAdminResponse getUserById(UUID id);
    UserAdminResponse deleteUser(UUID id);
    PageableResponse<UserAdminResponse> getUsers(int page, int size, String sortBy, String sortOrder);
    UserGrowthReportResponse getUserGrowth(LocalDate from, LocalDate to, String groupBy);
}
