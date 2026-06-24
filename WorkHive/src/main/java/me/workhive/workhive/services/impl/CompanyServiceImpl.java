package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.CompanyMapper;
import me.workhive.workhive.domain.dto.request.CreateCompanyRequest;
import me.workhive.workhive.domain.dto.request.UpdateCompanyRequest;
import me.workhive.workhive.domain.dto.response.CompanyResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.DuplicatedResourceException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.services.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;

    @Override
    public List<CompanyResponse> getAllCompanies() {

        List<Company> companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No companies found"
            );
        }

        return companyMapper.toDtoList(companies);
    }

    @Override
    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        if (companyRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicatedResourceException("Company name already exists");
        }
        Company company = companyMapper.toCompanyCreate(request);
        Company saved = companyRepository.save(company);
        return companyMapper.toDto(saved);
    }

    @Override
    public CompanyResponse getCompanyById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return companyMapper.toDto(company);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(UUID id, UpdateCompanyRequest request, User user) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (user.getRole() == Role.RECRUITER) {
            RecruiterProfile recruiter = recruiterRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
            if (recruiter.getCompany() == null || !recruiter.getCompany().getId().equals(id)) {
                throw new DeniedAccessException("You don't have permission to edit this company");
            }
        } else if (user.getRole() != Role.ADMINISTRATOR) {
            throw new DeniedAccessException("You don't have permission to edit this company");
        }

        if (!existingCompany.getName().equalsIgnoreCase(request.getName())) {
            companyRepository.findByName(request.getName()).ifPresent(c -> {
                throw new DuplicatedResourceException("Company name already exists");
            });
        }

        existingCompany.setName(request.getName());
        existingCompany.setLocation(request.getLocation());
        existingCompany.setSector(request.getSector());

        Company saved = companyRepository.save(existingCompany);
        return companyMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CompanyResponse deleteCompany(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        CompanyResponse response = companyMapper.toDto(company);
        companyRepository.delete(company);
        return response;
    }

    @Override
    public PageableResponse<CompanyResponse> getAllCompaniesAdmin(Pageable pageable) {
        Page<Company> companiesPage = companyRepository.findAll(pageable);

        if (companiesPage.isEmpty()) {
            throw new ResourceNotFoundException("No companies found");
        }

        List<CompanyResponse> companyResponses = companyMapper.toDtoList(companiesPage.getContent());

        return PageableResponse.<CompanyResponse>builder()
                .content(companyResponses)
                .page(companiesPage.getNumber())
                .size(companiesPage.getSize())
                .totalElements(companiesPage.getTotalElements())
                .totalPages(companiesPage.getTotalPages())
                .last(companiesPage.isLast())
                .build();
    }

    @Override
    public Map<String, Long> getGenderDiversityStats(UUID companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company not found");
        }

        List<Object[]> results = companyRepository.countGenderDiversityByCompanyId(companyId);

        Map<String, Long> stats = new HashMap<>();
        stats.put("M", 0L);
        stats.put("F", 0L);
        stats.put("O", 0L);

        for (Object[] row : results) {
            Gender gender = (Gender) row[0];
            Long count = (Long) row[1];
            if (gender != null) {
                switch (gender) {
                    case MALE -> stats.put("M", count);
                    case FEMALE -> stats.put("F", count);
                    case OTHER -> stats.put("O", count);
                }
            }
        }
        return stats;
    }



    @Override
    public CompanyResponse getMyCompany(User user) {
        RecruiterProfile recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        if (recruiter.getCompany() == null) {
            throw new ResourceNotFoundException("No company assigned to this recruiter");
        }
        return companyMapper.toDto(recruiter.getCompany());
    }
}