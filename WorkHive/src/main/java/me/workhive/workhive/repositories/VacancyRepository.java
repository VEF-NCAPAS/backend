package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.Vacancy;
import me.workhive.workhive.domain.entities.enums.Modality;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface VacancyRepository extends JpaRepository<Vacancy, UUID> {
    Page<Vacancy> findByStatus(VacancyStatus status, Pageable pageable);

    Page<Vacancy> findByTitleContainingIgnoreCaseAndStatus(String title, VacancyStatus status, Pageable pageable);

    Page<Vacancy> findByModalityAndStatus(Modality modality, VacancyStatus status, Pageable pageable);

    Page<Vacancy> findByTitleContainingIgnoreCaseAndModalityAndStatus(
            String title,
            Modality modality,
            VacancyStatus status,
            Pageable pageable
    );

    Page<Vacancy> findByCompany(Company company, Pageable pageable);

    Page<Vacancy> findByCompanyAndTitleContainingIgnoreCase(Company company, String title, Pageable pageable);

    Page<Vacancy> findByCompanyAndModality(Company company, Modality modality, Pageable pageable);

    Page<Vacancy> findByCompanyAndTitleContainingIgnoreCaseAndModality(
            Company company,
            String title,
            Modality modality,
            Pageable pageable
    );
    Page<Vacancy> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    Page<Vacancy> findByModality(
            Modality modality,
            Pageable pageable
    );

    Page<Vacancy> findByCompanyAndStatus(
            Company company,
            VacancyStatus status,
            Pageable pageable
    );

    Page<Vacancy> findByCompanyAndTitleContainingIgnoreCaseAndStatus(
            Company company,
            String title,
            VacancyStatus status,
            Pageable pageable
    );

    Page<Vacancy> findByCompanyAndModalityAndStatus(
            Company company,
            Modality modality,
            VacancyStatus status,
            Pageable pageable
    );

    Page<Vacancy> findByCompanyAndTitleContainingIgnoreCaseAndModalityAndStatus(
            Company company,
            String title,
            Modality modality,
            VacancyStatus status,
            Pageable pageable
    );

    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    long countByCreatedAtBetweenAndStatus(LocalDateTime from, LocalDateTime to, VacancyStatus status);
}
