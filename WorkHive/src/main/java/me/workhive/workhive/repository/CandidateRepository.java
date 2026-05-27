package me.workhive.workhive.repository;

import me.workhive.workhive.model.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateProfile, UUID> {
}
