package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequirementRepository extends JpaRepository<Requirement, UUID> {
    boolean existsByName(String name);
}
