package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CreateCompanyRequest;
import me.workhive.workhive.domain.dto.response.CompanyResponse;
import me.workhive.workhive.domain.entities.Company;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {

    public CompanyResponse toDto(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getName())
                .location(company.getLocation())
                .sector(company.getSector())
                .subscriptionPlan(company.getSubscriptionPlan())
                .build();
    }

    public List<CompanyResponse> toDtoList(List<Company> companies) {
        return companies.stream()
                .map(this::toDto)
                .toList();
    }

    public Company toCompanyCreate(CreateCompanyRequest request) {
        return Company.builder()
                .name(request.getName())
                .location(request.getLocation())
                .sector(request.getSector())
                .build();
    }
}