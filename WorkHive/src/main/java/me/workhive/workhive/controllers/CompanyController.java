package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CreateCompanyRequest;
import me.workhive.workhive.domain.dto.request.UpdateCompanyRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.services.CompanyService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping({"/company", "/companies", "/api/company", "/api/companies"})
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final ResponseFactory responseFactory;

    @GetMapping
    @Operation(
            summary = "Obtener todas las empresas",
            description = "Obtiene todas las empresas sin autenticacion para poder seleccionarlas durante el registro"
    )
    public ResponseEntity<GeneralResponse> getAllCompanies() {
        return responseFactory.buildResponse(
                "Companies retrieved successfully",
                HttpStatus.OK,
                companyService.getAllCompanies()
        );
    }

    @GetMapping("/my-company")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneralResponse> getMyCompany(@AuthenticationPrincipal User user) {
        return responseFactory.buildResponse(
                "My company retrieved successfully",
                HttpStatus.OK,
                companyService.getMyCompany(user)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return responseFactory.buildResponse(
                "Company created successfully",
                HttpStatus.CREATED,
                companyService.createCompany(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getCompanyById(@PathVariable UUID id) {
        return responseFactory.buildResponse(
                "Company retrieved successfully",
                HttpStatus.OK,
                companyService.getCompanyById(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECRUITER')")
    public ResponseEntity<GeneralResponse> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCompanyRequest request,
            @AuthenticationPrincipal User user
    ) {
        return responseFactory.buildResponse(
                "Company updated successfully",
                HttpStatus.OK,
                companyService.updateCompany(id, request, user)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> deleteCompany(@PathVariable UUID id) {
        return responseFactory.buildResponse(
                "Company deleted successfully",
                HttpStatus.OK,
                companyService.deleteCompany(id)
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getAllCompaniesAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return responseFactory.buildResponse(
                "Companies retrieved for admin successfully",
                HttpStatus.OK,
                companyService.getAllCompaniesAdmin(pageable)
        );
    }

    @GetMapping("/{id}/diversity")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECRUITER')")
    public ResponseEntity<GeneralResponse> getCompanyDiversityStats(@PathVariable UUID id) {
        return responseFactory.buildResponse(
                "Company gender diversity statistics retrieved successfully",
                HttpStatus.OK,
                companyService.getGenderDiversityStats(id)
        );
    }

    @GetMapping("/diversity")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<GeneralResponse> getGlobalDiversityStats() {
        return responseFactory.buildResponse(
                "Global gender diversity statistics retrieved successfully",
                HttpStatus.OK,
                companyService.getGlobalGenderDiversityStats()
        );
    }
}