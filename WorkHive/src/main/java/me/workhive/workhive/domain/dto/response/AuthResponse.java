package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Role;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String email;

    private Role role;

    private String message;
}
