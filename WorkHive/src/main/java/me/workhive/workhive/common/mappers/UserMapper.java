package me.workhive.workhive.common.mappers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.UpdateUserRequest;
import me.workhive.workhive.domain.dto.response.UserAdminResponse;
import me.workhive.workhive.domain.dto.response.UserResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .build();
    }

    public UserAdminResponse toUsersDto(User user) {
        return UserAdminResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
    }

    public void updateUser(
            User user,
            UpdateUserRequest request
    ) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
    }

    public Page<UserAdminResponse> toDtoUsersList(Page<User> users) {
        return users
                .map(this::toUsersDto);

    }
}
