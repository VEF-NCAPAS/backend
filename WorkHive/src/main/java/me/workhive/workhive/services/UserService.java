package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.entities.User;

public interface UserService {
    void changePassword(User user, ChangePasswordRequest request);
}
