package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;

@Data
public class CandidateRegisterRequest {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Name must contain only letters")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Email(message = "Email invalid format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, number and special character"
    )
    @NotBlank(message = "Password is required")
    private String password;
}
