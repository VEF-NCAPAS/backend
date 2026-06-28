package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.AuthMapper;
import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.LoginRequest;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.response.AuthResponse;
import me.workhive.workhive.domain.dto.response.RegisterResponse;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.exceptions.DuplicatedResourceException;
import me.workhive.workhive.exceptions.InvalidCredentialsException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
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
    private final AuthMapper authMapper;

    @Override
    public RegisterResponse registerCandidate(CandidateRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedResourceException("Email already exists");
        }
        User user = authMapper.toUserCandidateCreate(request, passwordEncoder);
        userRepository.save(user);

        CandidateProfile profile = authMapper.toCandidateProfile(user);
        userRepository.save(user);

        candidateProfileRepository.save(profile);

        return authMapper.toRegisterDto(user);
    }

    @Override
    public RegisterResponse registerRecruiter(RecruiterRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedResourceException("Email already exists");
        }

        Company company;

        if (request.getCompany().getCompanyId() != null) {

            company = companyRepository.findById(request.getCompany().getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        } else {

            if (companyRepository.existsByNameIgnoreCase(request.getCompany().getCompanyName())) {
                throw new DuplicatedResourceException("Company already exists");
            }
            company = companyRepository.save(authMapper.toCompanyCreate(request.getCompany()));
        }

        User user = authMapper.toUserRecruiterCreate(request, passwordEncoder);
        userRepository.save(user);

        RecruiterProfile profile = authMapper.toRecruiterProfile(user, company);
        recruiterProfileRepository.save(profile);

        return authMapper.toRegisterDto(user);

    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User no registered"));

        boolean isValidPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isValidPassword) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return authMapper.toAuthDto(user, token);
    }

}
