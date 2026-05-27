package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.LoginRequest;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.response.AuthResponse;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.repositories.CandidateRepository;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.services.AuthService;
import me.workhive.workhive.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterRepository recruiterProfileRepository;
    private final CandidateRepository candidateProfileRepository;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse registerCandidate(CandidateRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(Role.CANDIDATE)
                .build();

        userRepository.save(user);

        CandidateProfile candidateProfile = CandidateProfile.builder()
                .user(user)
                .build();

        candidateProfileRepository.save(candidateProfile);

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole(),
                "Logged in successfully as " + user.getRole().name()
        );
    }

    @Override
    public AuthResponse registerRecruiter(RecruiterRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Company company;

        if (request.getCompanyId() != null) {

            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() ->
                    new RuntimeException("Company not found"));

        } else {
            company = Company.builder()
                    .name(request.getCompanyName())
                    .location(request.getLocation())
                    .sector(request.getSector())
                    .build();

            companyRepository.save(company);
        }

        User user = User.builder()
                .name(request.getName())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(Role.RECRUITER)
                .build();

        userRepository.save(user);

        RecruiterProfile recruiterProfile =
                RecruiterProfile.builder()
                        .user(user)
                        .company(company)
                        .build();

        recruiterProfileRepository.save(recruiterProfile);

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole(),
                "Logged in successfully as " + user.getRole().name()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        boolean isValidPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isValidPassword) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole(),
                "Logged in successfully as " + user.getRole().name()
        );
    }

}
