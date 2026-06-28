package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateProfile, UUID> {
    Optional<CandidateProfile> findByUser(User user);

}
