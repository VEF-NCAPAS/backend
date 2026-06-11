package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Role;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String name;
    
    private String token;

    private String email;

    private Role role;

}
