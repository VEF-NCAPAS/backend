package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT u.gender, COUNT(DISTINCT c.id) " +
           "FROM Company co " +
           "JOIN co.vacancies v " +
           "JOIN v.applications a " +
           "JOIN a.candidate c " +
           "JOIN c.user u " +
           "WHERE co.id = :companyId " +
           "GROUP BY u.gender")
    List<Object[]> countGenderDiversityByCompanyId(@Param("companyId") UUID companyId);
}
