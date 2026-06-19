package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;

@Data
public class AdminCreateUserRequest {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-ZÃ¡Ã©Ã­Ã³ÃºÃÃ‰ÃÃ“ÃšÃ±Ã‘ ]+$", message = "Name must contain only letters")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Email(message = "Email invalid format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, number and special character"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    private CompanySelection company;
}
