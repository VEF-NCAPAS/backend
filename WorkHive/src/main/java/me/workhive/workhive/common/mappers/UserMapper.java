package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.response.UserResponse;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserDto(User user) {
        RecruiterProfile recruiterProfile = user.getRecruiterProfile();

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .gender(user.getGender())
                .email(user.getEmail())
                .role(user.getRole())
                .companyId(recruiterProfile != null && recruiterProfile.getCompany() != null
                        ? recruiterProfile.getCompany().getId()
                        : null)
                .companyName(recruiterProfile != null && recruiterProfile.getCompany() != null
                        ? recruiterProfile.getCompany().getName()
                        : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}