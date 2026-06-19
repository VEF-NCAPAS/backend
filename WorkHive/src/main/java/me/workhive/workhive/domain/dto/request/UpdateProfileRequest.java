package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;

@Data
public class UpdateProfileRequest {
    @Pattern(regexp = "^[a-zA-ZÃ¡Ã©Ã­Ã³ÃºÃÃ‰ÃÃ“ÃšÃ±Ã‘ ]+$", message = "Name must contain only letters")
    private String name;

    private Gender gender;

    @Email(message = "Email invalid format")
    private String email;

    private CompanySelection company;
}
