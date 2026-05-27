package me.workhive.workhive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.workhive.workhive.model.enums.Role;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String email;

    private Role role;

    private String message;
}
