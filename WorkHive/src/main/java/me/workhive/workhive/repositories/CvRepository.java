package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CvRepository extends JpaRepository<Cv, UUID> {
    Optional<Cv> findByCandidateProfile(CandidateProfile candidateProfile);

    boolean existsByCandidateProfile(CandidateProfile candidateProfile);

}
