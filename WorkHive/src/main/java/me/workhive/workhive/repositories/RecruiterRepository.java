package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecruiterRepository extends JpaRepository<RecruiterProfile, UUID> {
}
