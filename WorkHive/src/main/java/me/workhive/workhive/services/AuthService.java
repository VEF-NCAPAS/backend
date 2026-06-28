package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.LoginRequest;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.response.AuthResponse;
import me.workhive.workhive.domain.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCandidate(
            CandidateRegisterRequest request
    );

    RegisterResponse registerRecruiter(
            RecruiterRegisterRequest request
    );

    AuthResponse login(
            LoginRequest request
    );
}
