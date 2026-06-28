package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;

import java.util.UUID;

@Data
@Builder
public class UserAdminResponse {

    private UUID id;

    private String name;

    private String email;

    private Gender gender;

    private Role role;

}