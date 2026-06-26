package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.UserMapper;
import me.workhive.workhive.domain.dto.request.UpdateUserRequest;
import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.dto.response.*;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DuplicatedResourceException;
import me.workhive.workhive.exceptions.InvalidCredentialsException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
    public Map<String, Long> getGlobalGenderDiversityStats() {
        List<Object[]> results = userRepository.countAllUsersByGender();

        Map<String, Long> stats = new HashMap<>();
        stats.put("M", 0L);
        stats.put("F", 0L);
        stats.put("O", 0L);

        for (Object[] row : results) {
            Gender gender = (Gender) row[0];
            Long count = (Long) row[1];
            if (gender != null) {
                switch (gender) {
                    case MALE -> stats.put("M", count);
                    case FEMALE -> stats.put("F", count);
                    case OTHER -> stats.put("O", count);
                }
            }
        }
        return stats;
    }

    @Override
    public UserResponse updateMyProfile(User currentUser, UpdateUserRequest request) {

        if (!currentUser.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicatedResourceException("Email already exists");
        }

        userMapper.updateUser(currentUser, request);

        return userMapper.toDto(userRepository.save(currentUser));
    }

    @Override
    public UserResponse adminUpdateUser(UUID userId, UpdateUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicatedResourceException("Email already exists");
        }

        userMapper.updateUser(user, request);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserAdminResponse getUserById(UUID id) {
        return userMapper.toUsersDto(
                this.findUserById(id)
        );
    }

    @Override
    @Transactional
    public UserAdminResponse deleteUser(UUID id) {
        User user = this.findUserById(id);

        UserAdminResponse userAdminResponse = userMapper.toUsersDto(user);

        userRepository.delete(user);
        return userAdminResponse;
    }

    @Override
    public PageableResponse<UserAdminResponse> getUsers(int page, int size, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<User> users= userRepository.findAll(pageable);

        Page<UserAdminResponse> adminUserPage = userMapper.toDtoUsersList(users);

        if (adminUserPage.getTotalElements() == 0) {

            throw new ResourceNotFoundException("No users found");
        }


        return PageableResponse
                .<UserAdminResponse>builder()
                .content(adminUserPage.getContent())
                .page(adminUserPage.getNumber())
                .size(adminUserPage.getSize())
                .totalElements(adminUserPage.getTotalElements())
                .totalPages(adminUserPage.getTotalPages())
                .last(adminUserPage.isLast())
                .build();
    }


    public User findUserById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }
}
