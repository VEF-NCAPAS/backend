package me.workhive.workhive.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;

import java.util.UUID;

@Data
public class RecruiterRegisterRequest {

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Name must contain only letters")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Email(message = "Email invalid format")
    @NotBlank(message = "Email is required")
    @Schema(example = "email@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, number and special character"
    )    private String password;
    @NotNull(message = "Company selection is required")
    private CompanySelection company;
}
