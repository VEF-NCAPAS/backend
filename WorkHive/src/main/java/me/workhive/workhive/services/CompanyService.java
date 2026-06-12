package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.response.CompanyResponse;

import java.util.List;

public interface CompanyService {

    List<CompanyResponse> getAllCompanies();

}