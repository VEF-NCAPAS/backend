package me.workhive.workhive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.dto.request.LoginRequest;
import me.workhive.workhive.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.dto.response.AuthResponse;
import me.workhive.workhive.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register/candidate")
    public ResponseEntity<AuthResponse> registerCandidate(@Valid @RequestBody CandidateRegisterRequest registerRequest){
        return ResponseEntity.ok(
                authService.registerCandidate(registerRequest)
        );
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<AuthResponse> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest registerRequest){
        return ResponseEntity.ok(
                authService.registerRecruiter(registerRequest)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

}
