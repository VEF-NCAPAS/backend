package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    boolean existsByName(String name);
}
