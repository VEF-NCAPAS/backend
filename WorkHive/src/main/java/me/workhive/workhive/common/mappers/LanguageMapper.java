package me.workhive.workhive.common.mappers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.LanguageSelection;
import me.workhive.workhive.domain.dto.response.LanguageResponse;
import me.workhive.workhive.domain.entities.*;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageResponse toDto(Language language) {
        return LanguageResponse.builder()
                .id(language.getId())
                .name(language.getName())
                .build();
    }

    public CvLanguage toCvLanguage(LanguageSelection request, Language language) {
        return CvLanguage.builder()
                .language(language)
                .level(request.getLevel())
                .build();
    }
}