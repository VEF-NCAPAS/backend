package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.TechnicalTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TechnicalTestRepository extends JpaRepository<TechnicalTest, UUID> {
}
