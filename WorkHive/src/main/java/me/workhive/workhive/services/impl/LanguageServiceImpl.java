package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.LanguageMapper;
import me.workhive.workhive.common.mappers.SkillMapper;
import me.workhive.workhive.domain.dto.response.LanguageResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.SkillResponse;
import me.workhive.workhive.domain.entities.Language;
import me.workhive.workhive.domain.entities.Skill;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.LanguageRepository;
import me.workhive.workhive.repositories.SkillRepository;
import me.workhive.workhive.services.LanguageService;
import me.workhive.workhive.services.SkillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public List<LanguageResponse> getAll() {
        List<Language> languages = languageRepository.findAll();

        if (languages.isEmpty()){
            throw new ResourceNotFoundException("No languages found");
        }
        return languages.stream()
                .map(languageMapper::toDto)
                .toList();
    }

    @Override
    public LanguageResponse getById(UUID id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Languages not found")
                );
        return languageMapper.toDto(language);
    }
}