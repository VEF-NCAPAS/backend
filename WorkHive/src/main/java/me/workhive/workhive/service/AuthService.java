package me.workhive.workhive.service;

import me.workhive.workhive.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.dto.request.LoginRequest;
import me.workhive.workhive.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.dto.response.AuthResponse;

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
