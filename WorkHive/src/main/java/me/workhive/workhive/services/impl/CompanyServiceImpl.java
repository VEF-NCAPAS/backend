package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.CompanyMapper;
import me.workhive.workhive.domain.dto.response.CompanyResponse;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.services.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

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
}