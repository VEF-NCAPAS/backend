package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Language;
import me.workhive.workhive.domain.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LanguageRepository extends JpaRepository<Language, UUID> {
    boolean existsByName(String name);
}
