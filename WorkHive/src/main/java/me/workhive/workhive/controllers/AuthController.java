package me.workhive.workhive.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.LoginRequest;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.response.GeneralResponse;
import me.workhive.workhive.services.AuthService;
import me.workhive.workhive.utils.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseFactory responseFactory;

    @PostMapping("/register/candidate")
    public ResponseEntity<GeneralResponse> registerCandidate(@Valid @RequestBody CandidateRegisterRequest registerRequest){
        return responseFactory.buildResponse(
                "Candidate registered",
                HttpStatus.CREATED,
                authService.registerCandidate(registerRequest)
        );
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<GeneralResponse> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest registerRequest){
        return responseFactory.buildResponse(
                "Recruiter registered",
                HttpStatus.CREATED,
                authService.registerRecruiter(registerRequest)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginRequest request) {
        return responseFactory.buildResponse(
                "Login successful",
                HttpStatus.OK,
                authService.login(request)

        );
    }

}
