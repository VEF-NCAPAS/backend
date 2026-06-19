package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.UserMapper;
import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.dto.request.CompanySelection;
import me.workhive.workhive.domain.dto.request.UpdateProfileRequest;
import me.workhive.workhive.domain.dto.response.UserResponse;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DuplicatedResourceException;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.InvalidCredentialsException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public void changePassword(User user, ChangePasswordRequest request) {

        boolean matches = passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(User user, UpdateProfileRequest request) {
        if (user.getRole() != Role.CANDIDATE && user.getRole() != Role.RECRUITER) {
            throw new DeniedAccessException("Role not authorized");
        }

        updateUserFields(user, request.getName(), request.getGender(), request.getEmail());

        if (user.getRole() == Role.RECRUITER && request.getCompany() != null) {
            RecruiterProfile recruiter = recruiterRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
            recruiter.setCompany(resolveCompany(request.getCompany()));
            recruiterRepository.save(recruiter);
            user.setRecruiterProfile(recruiter);
        }

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserResponse getProfile(User user) {
        return userMapper.toUserDto(user);
    }

    private void updateUserFields(User user, String name, me.workhive.workhive.domain.entities.enums.Gender gender, String email) {
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (gender != null) {
            user.setGender(gender);
        }

        if (email != null && !email.isBlank() && !email.equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new DuplicatedResourceException("Email already exists");
            }
            user.setEmail(email);
        }
    }

    private Company resolveCompany(CompanySelection selection) {
        if (selection.getCompanyId() != null) {
            return companyRepository.findById(selection.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        }

        if (selection.getCompanyName() == null || selection.getCompanyName().isBlank()) {
            throw new BusinessRuleException("Company name is required");
        }

        return companyRepository.findByName(selection.getCompanyName())
                .orElseGet(() -> companyRepository.save(
                Company.builder()
                        .name(selection.getCompanyName())
                        .location(selection.getLocation())
                        .sector(selection.getSector())
                        .build()
        ));
    }
}
