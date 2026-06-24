package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.enums.LanguageLevel;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponse {

    private UUID id;
    private String name;
}