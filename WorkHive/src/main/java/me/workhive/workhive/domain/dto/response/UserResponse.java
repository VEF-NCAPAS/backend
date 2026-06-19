package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String name;
    private Gender gender;
    private String email;
    private Role role;
    private UUID companyId;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
}
