package me.workhive.workhive.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.services.LanguageService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;
    private final ResponseFactory responseFactory;

    @GetMapping
    @Operation(
            summary = "Obtener todos los idiomas",
            description = "Usuario obtiene todos los idiomas disponibles"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<GeneralResponse> getAll() {
        return responseFactory.buildResponse(
                "Languages retrieved successfully",
                HttpStatus.OK,
                languageService.getAll()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener idiomas por id",
            description = "Usuario obtiene los idiomas disponibles por id"
    )
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<GeneralResponse> getById(
            @PathVariable UUID id
    ) {
        return responseFactory.buildResponse(
                "Language retrieved successfully",
                HttpStatus.OK,
                languageService.getById(id)
        );
    }
}