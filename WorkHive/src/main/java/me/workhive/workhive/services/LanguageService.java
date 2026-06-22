package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.response.LanguageResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.SkillResponse;

import java.util.List;
import java.util.UUID;

public interface LanguageService {

    List<LanguageResponse> getAll();

    LanguageResponse getById(UUID id);
}
