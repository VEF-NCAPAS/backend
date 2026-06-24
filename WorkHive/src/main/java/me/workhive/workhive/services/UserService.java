package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.entities.User;

import java.util.Map;

public interface UserService {
    void changePassword(User user, ChangePasswordRequest request);

    Map<String, Long> getGlobalGenderDiversityStats();
}
