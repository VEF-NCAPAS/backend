package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.response.SkillResponse;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<SkillResponse> getAllSkills();

    SkillResponse getSkillsById(UUID id);
}
