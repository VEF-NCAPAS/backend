package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreateCompanyRequest;
import me.workhive.workhive.domain.dto.request.UpdateCompanyRequest;
import me.workhive.workhive.domain.dto.response.CompanyResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CompanyService {

    List<CompanyResponse> getAllCompanies();

    CompanyResponse createCompany(CreateCompanyRequest request);

    CompanyResponse getCompanyById(UUID id);

    CompanyResponse updateCompany(UUID id, UpdateCompanyRequest request, User user);

    CompanyResponse deleteCompany(UUID id);

    PageableResponse<CompanyResponse> getAllCompaniesAdmin(Pageable pageable);

    Map<String, Long> getGenderDiversityStats(UUID companyId);

    CompanyResponse getMyCompany(User user);
}