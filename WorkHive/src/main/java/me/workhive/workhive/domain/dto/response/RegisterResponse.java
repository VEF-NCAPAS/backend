package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;


@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String name;
    private String email;
    private Gender gender;
    private Role role;
}
