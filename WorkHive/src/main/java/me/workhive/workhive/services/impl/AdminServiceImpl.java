package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.UserMapper;
import me.workhive.workhive.domain.dto.request.AdminCreateCandidateRequest;
import me.workhive.workhive.domain.dto.request.AdminCreateRecruiterRequest;
import me.workhive.workhive.domain.dto.request.AdminCreateUserRequest;
import me.workhive.workhive.domain.dto.request.AdminUpdateUserRequest;
import me.workhive.workhive.domain.dto.request.CompanySelection;
import me.workhive.workhive.domain.dto.response.ApplicationVolumeReportResponse;
import me.workhive.workhive.domain.dto.response.PageableResponse;
import me.workhive.workhive.domain.dto.response.UserGrowthItemResponse;
import me.workhive.workhive.domain.dto.response.UserGrowthReportResponse;
import me.workhive.workhive.domain.dto.response.UserResponse;
import me.workhive.workhive.domain.dto.response.VacancyVolumeReportResponse;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.domain.entities.enums.VacancyStatus;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DuplicatedResourceException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.ApplicationRepository;
import me.workhive.workhive.repositories.CandidateRepository;
import me.workhive.workhive.repositories.CompanyRepository;
import me.workhive.workhive.repositories.RecruiterRepository;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.repositories.VacancyRepository;
import me.workhive.workhive.services.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;
    private final VacancyRepository vacancyRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(AdminCreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedResourceException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        if (request.getRole() == Role.CANDIDATE) {
            CandidateProfile candidateProfile = candidateRepository.save(
                    CandidateProfile.builder()
                            .user(savedUser)
                            .build()
            );
            savedUser.setCandidateProfile(candidateProfile);
        }

        if (request.getRole() == Role.RECRUITER) {
            if (request.getCompany() == null) {
                throw new BusinessRuleException("Company is required for recruiter users");
            }

            RecruiterProfile recruiterProfile = recruiterRepository.save(
                    RecruiterProfile.builder()
                            .user(savedUser)
                            .company(resolveCompany(request.getCompany()))
                            .build()
            );
            savedUser.setRecruiterProfile(recruiterProfile);
        }

        return userMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponse createCandidate(AdminCreateCandidateRequest request) {
        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest();
        createUserRequest.setName(request.getName());
        createUserRequest.setGender(request.getGender());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setPassword(request.getPassword());
        createUserRequest.setRole(Role.CANDIDATE);

        return createUser(createUserRequest);
    }

    @Override
    @Transactional
    public UserResponse createRecruiter(AdminCreateRecruiterRequest request) {
        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest();
        createUserRequest.setName(request.getName());
        createUserRequest.setGender(request.getGender());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setPassword(request.getPassword());
        createUserRequest.setRole(Role.RECRUITER);
        createUserRequest.setCompany(request.getCompany());

        return createUser(createUserRequest);
    }

    @Override
    public PageableResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortOrder, Role role, String search) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAllForAdmin(role, search, pageable);

        Page<UserResponse> userPage = users.map(userMapper::toUserDto);

        return PageableResponse.<UserResponse>builder()
                .content(userPage.getContent())
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .last(userPage.isLast())
                .build();
    }

    @Override
    public PageableResponse<UserResponse> getCandidates(int page, int size, String sortBy, String sortOrder, String search) {
        return getAllUsers(page, size, sortBy, sortOrder, Role.CANDIDATE, search);
    }

    @Override
    public PageableResponse<UserResponse> getRecruiters(int page, int size, String sortBy, String sortOrder, String search) {
        return getAllUsers(page, size, sortBy, sortOrder, Role.RECRUITER, search);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userMapper.toUserDto(findUser(id));
    }

    @Override
    public UserResponse getCandidateById(UUID id) {
        return userMapper.toUserDto(findUserByRole(id, Role.CANDIDATE));
    }

    @Override
    public UserResponse getRecruiterById(UUID id) {
        return userMapper.toUserDto(findUserByRole(id, Role.RECRUITER));
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, AdminUpdateUserRequest request) {
        User user = findUser(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicatedResourceException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

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
    @Transactional
    public UserResponse updateCandidate(UUID id, AdminUpdateUserRequest request) {
        findUserByRole(id, Role.CANDIDATE);
        return updateUser(id, request);
    }

    @Override
    @Transactional
    public UserResponse updateRecruiter(UUID id, AdminUpdateUserRequest request) {
        findUserByRole(id, Role.RECRUITER);
        return updateUser(id, request);
    }

    @Override
    @Transactional
    public UserResponse deleteUser(UUID id, User currentUser) {
        User user = findUser(id);

        if (user.getId().equals(currentUser.getId())) {
            throw new BusinessRuleException("Administrator cannot delete itself");
        }

        UserResponse response = userMapper.toUserDto(user);
        userRepository.delete(user);
        return response;
    }

    @Override
    @Transactional
    public UserResponse deleteCandidate(UUID id, User currentUser) {
        findUserByRole(id, Role.CANDIDATE);
        return deleteUser(id, currentUser);
    }

    @Override
    @Transactional
    public UserResponse deleteRecruiter(UUID id, User currentUser) {
        findUserByRole(id, Role.RECRUITER);
        return deleteUser(id, currentUser);
    }

    @Override
    public VacancyVolumeReportResponse getVacancyVolume(LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        LocalDateTime fromDateTime = startOfDay(from);
        LocalDateTime toDateTime = endOfDay(to);

        return VacancyVolumeReportResponse.builder()
                .from(from)
                .to(to)
                .created(vacancyRepository.countByCreatedAtBetween(fromDateTime, toDateTime))
                .active(vacancyRepository.countByCreatedAtBetweenAndStatus(fromDateTime, toDateTime, VacancyStatus.OPEN))
                .closed(vacancyRepository.countByCreatedAtBetweenAndStatus(fromDateTime, toDateTime, VacancyStatus.CLOSE))
                .build();
    }

    @Override
    public ApplicationVolumeReportResponse getApplicationVolume(LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        return ApplicationVolumeReportResponse.builder()
                .from(from)
                .to(to)
                .totalApplications(applicationRepository.countByCreatedAtBetween(startOfDay(from), endOfDay(to)))
                .build();
    }

    @Override
    public UserGrowthReportResponse getUserGrowth(LocalDate from, LocalDate to, String groupBy) {
        validateDateRange(from, to);
        String normalizedGroupBy = groupBy == null ? "day" : groupBy.toLowerCase();

        if (!normalizedGroupBy.equals("day") && !normalizedGroupBy.equals("month")) {
            throw new BusinessRuleException("groupBy must be day or month");
        }

        List<User> users = userRepository.findByCreatedAtBetweenAndRoleIn(
                startOfDay(from),
                endOfDay(to),
                List.of(Role.CANDIDATE, Role.RECRUITER)
        );

        Map<String, long[]> grouped = new LinkedHashMap<>();
        users.forEach(user -> {
            String period = normalizedGroupBy.equals("month")
                    ? YearMonth.from(user.getCreatedAt()).toString()
                    : user.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE);

            long[] counts = grouped.computeIfAbsent(period, key -> new long[2]);
            if (user.getRole() == Role.CANDIDATE) {
                counts[0]++;
            } else if (user.getRole() == Role.RECRUITER) {
                counts[1]++;
            }
        });

        List<UserGrowthItemResponse> growth = grouped.entrySet()
                .stream()
                .map(entry -> UserGrowthItemResponse.builder()
                        .period(entry.getKey())
                        .candidates(entry.getValue()[0])
                        .recruiters(entry.getValue()[1])
                        .build())
                .toList();

        return UserGrowthReportResponse.builder()
                .from(from)
                .to(to)
                .groupBy(normalizedGroupBy)
                .growth(growth)
                .build();
    }

    private User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findUserByRole(UUID id, Role role) {
        User user = findUser(id);

        if (user.getRole() != role) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;
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

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BusinessRuleException("from date must be before or equal to to date");
        }
    }

    private LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime endOfDay(LocalDate date) {
        return date.plusDays(1).atStartOfDay().minusNanos(1);
    }
}
