package me.workhive.workhive.config;

import lombok.RequiredArgsConstructor;
import me.workhive.workhive.domain.entities.Requirement;
import me.workhive.workhive.domain.entities.Skill;
import me.workhive.workhive.repositories.RequirementRepository;
import me.workhive.workhive.repositories.SkillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SkillRepository skillRepository;
    private final RequirementRepository requirementRepository;

    @Override
    public void run(String... args){
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
