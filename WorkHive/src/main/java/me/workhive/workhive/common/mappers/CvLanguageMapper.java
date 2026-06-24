package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.LanguageSelection;
import me.workhive.workhive.domain.dto.response.CvLanguageResponse;
import me.workhive.workhive.domain.dto.response.LanguageResponse;
import me.workhive.workhive.domain.entities.CvLanguage;
import me.workhive.workhive.domain.entities.Language;
import org.springframework.stereotype.Component;

@Component
public class CvLanguageMapper {

    public CvLanguageResponse toDto(CvLanguage cvLanguage) {
        return CvLanguageResponse.builder()
                .id(cvLanguage.getLanguage().getId())
                .name(cvLanguage.getLanguage().getName())
                .level(cvLanguage.getLevel())
                .build();
    }
}