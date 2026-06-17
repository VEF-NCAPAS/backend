package me.workhive.workhive.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.domain.entities.Skill;
import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Gender;
import me.workhive.workhive.domain.entities.enums.Role;
import me.workhive.workhive.repositories.RequirementRepository;
import me.workhive.workhive.repositories.SkillRepository;
import me.workhive.workhive.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SkillRepository skillRepository;
    private final RequirementRepository requirementRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    Dotenv dotenv = Dotenv.load();
    String adminEmail = dotenv.get("MAIL_USERNAME");
    String adminPassword = dotenv.get("ADMIN_PASSWORD");

    @Override
    public void run(String... args){
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .name("Administrador")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMINISTRATOR)
                    .gender(Gender.OTHER)
                    .build();

            userRepository.save(admin);
        }
        List<String> technologies = List.of(
                "Java",
                "Spring Boot",
                "JavaScript",
                "TypeScript",
                "React",
                "Angular",
                "Node.js",
                "Express.js",
                "PostgreSQL",
                "MySQL",
                "MongoDB",
                "Docker",
                "Kubernetes",
                "Git",
                "GitHub",
                "REST API",
                "Microservices",
                "AWS",
                "Azure",
                "Linux"
        );

        technologies.forEach(skillName -> {
            if (!skillRepository.existsByName(skillName)) {
                skillRepository.save(
                        Skill.builder()
                                .name(skillName)
                                .build()
                );
            }
        });

        technologies.forEach(requirementName -> {
            if (!requirementRepository.existsByName(requirementName)) {
                requirementRepository.save(
                        Requirement.builder()
                                .name(requirementName)
                                .build()
                );
            }
        });

    }
}
