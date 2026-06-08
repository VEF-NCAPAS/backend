package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Page<Application> findByCandidate_Id(UUID candidateId, Pageable pageable);

    Page<Application> findByVacancy_CompanyId(UUID vacancyCompanyId, Pageable pageable);
    boolean existsByCandidateAndVacancy(CandidateProfile candidate, Vacancy vacancy);
}
