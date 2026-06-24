package me.workhive.workhive.services.impl;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.dto.request.ChangePasswordRequest;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.exceptions.InvalidCredentialsException;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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
}
