package me.workhive.workhive.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.workhive.workhive.model.enums.Gender;

@Data
public class CandidateRegisterRequest {
    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @Email
    private String email;

    @NotBlank
    private String password;
}
