package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.dto.request.UpdateProfileRequest;
import me.workhive.workhive.domain.dto.response.UserResponse;
import me.workhive.workhive.domain.entities.User;

public interface UserService {
    void changePassword(User user, ChangePasswordRequest request);
    UserResponse updateProfile(User user, UpdateProfileRequest request);
    UserResponse getProfile(User user);
}
