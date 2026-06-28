package me.workhive.workhive.common.mappers;

import me.workhive.workhive.domain.dto.request.CandidateRegisterRequest;
import me.workhive.workhive.domain.dto.request.CompanySelection;
import me.workhive.workhive.domain.dto.request.RecruiterRegisterRequest;
import me.workhive.workhive.domain.dto.response.AuthResponse;
import me.workhive.workhive.domain.dto.response.RegisterResponse;
import me.workhive.workhive.domain.entities.CandidateProfile;
import me.workhive.workhive.domain.entities.Company;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public User toUserCandidateCreate(CandidateRegisterRequest request, PasswordEncoder passwordEncoder){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CANDIDATE)
                .build();
    }
    public User toUserRecruiterCreate(RecruiterRegisterRequest request, PasswordEncoder passwordEncoder){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.RECRUITER)
                .build();
    }

    public Company toCompanyCreate(CompanySelection selection){
        return Company.builder()
                .name(selection.getCompanyName())
                .location(selection.getLocation())
                .sector(selection.getSector())
                .build();
    }

    public CandidateProfile toCandidateProfile(User user){
        return CandidateProfile.builder()
                .user(user)
                .build();
    }
    public RecruiterProfile toRecruiterProfile(User user, Company company){
        return RecruiterProfile.builder()
                .user(user)
                .company(company)
                .build();
    }

//    para desarrollo mostrar token, luego cambiar
    public AuthResponse toAuthDto(User user, String token) {
        return AuthResponse.builder()
                .name(user.getName())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public RegisterResponse toRegisterDto(User user) {
        return RegisterResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
    }
}
