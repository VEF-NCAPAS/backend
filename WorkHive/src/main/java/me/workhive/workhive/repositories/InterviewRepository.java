package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {
}
