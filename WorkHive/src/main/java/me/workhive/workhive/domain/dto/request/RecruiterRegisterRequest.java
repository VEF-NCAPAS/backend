package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;

import java.util.UUID;

@Data
public class RecruiterRegisterRequest {
    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @Email
    private String email;

    @NotBlank
    private String password;

    private UUID companyId;

    private String companyName;

    private String location;

    private String sector;
}
