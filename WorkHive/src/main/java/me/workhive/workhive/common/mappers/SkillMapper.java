package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.SkillSelection;
import me.workhive.workhive.domain.dto.response.SkillResponse;
import me.workhive.workhive.domain.entities.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillResponse toSkillDto(Skill skill){
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }
    
}
