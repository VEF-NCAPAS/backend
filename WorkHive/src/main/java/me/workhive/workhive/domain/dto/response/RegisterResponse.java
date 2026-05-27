package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Role;


@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String email;

    private Role role;
}
