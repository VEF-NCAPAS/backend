package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.SkillMapper;
import me.workhive.workhive.domain.dto.response.SkillResponse;
import me.workhive.workhive.domain.entities.Skill;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.SkillRepository;
import me.workhive.workhive.services.SkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Override
    public List<SkillResponse> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();

        if (skills.isEmpty()){
            throw new ResourceNotFoundException("No skills found");
        }
        return skills.stream()
                .map(skillMapper::toSkillDto)
                .toList();
    }

    @Override
    public SkillResponse getSkillsById(UUID id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skills not found")
                );
        return skillMapper.toSkillDto(skill);
    }
}