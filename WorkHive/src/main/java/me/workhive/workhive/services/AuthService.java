package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.LoginRequest;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerCandidate(
            CandidateRegisterRequest request
    );

    AuthResponse registerRecruiter(
            RecruiterRegisterRequest request
    );

    AuthResponse login(
            LoginRequest request
    );
}
