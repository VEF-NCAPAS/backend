package me.workhive.workhive.controllers;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.services.SkillService;
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
@RequestMapping("/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;
    private final ResponseFactory responseFactory;

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> getAllSkills(){
        return responseFactory.buildResponse(
                "Skills retrieved successfully",
                HttpStatus.OK,
                skillService.getAllSkills()
        );
    }

    @GetMapping(("/{id}"))
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<GeneralResponse> getSkillsById(
            @PathVariable UUID id
            ){
        return responseFactory.buildResponse(
                "Skill retrieved successfully",
                HttpStatus.OK,
                skillService.getSkillsById(id)
        );
    }
}
