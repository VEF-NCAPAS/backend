package me.workhive.workhive.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email invalid format")
    @NotBlank(message = "Email is required")

    @Schema(example = "email@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
