package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Page<Application> findByCandidate_Id(UUID candidateId, Pageable pageable);

    Page<Application> findByVacancy_CompanyId(UUID vacancyCompanyId, Pageable pageable);
    boolean existsByCandidateAndVacancy(CandidateProfile candidate, Vacancy vacancy);
    Page<Application> findByVacancy_Id(UUID vacancyId, Pageable pageable);
    Page<Application> findByVacancy_IdAndCv_Skills_NameIgnoreCase(
            UUID vacancyId,
            String skillName,
            Pageable pageable
    );
    @Query("""
    SELECT a
    FROM Application a
    WHERE a.applicationStatus = me.workhive.workhive.domain.entities.enums.ApplicationStatus.SELECTED
      AND a.vacancy.company.id = :companyId
""")
    List<Application> findSelectedApplicationsByCompany(UUID companyId);
}
