package me.workhive.workhive.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.workhive.workhive.domain.entities.enums.LanguageLevel;

import java.util.UUID;

@Data
public class LanguageSelection {

    @NotNull(message = "Language is required")
    private UUID id;

    @NotNull(message = "Level is required")
    private LanguageLevel level;
}