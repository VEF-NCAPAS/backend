package me.workhive.workhive.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.enums.LanguageLevel;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "cv")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "candidateProfileId", nullable = false)
    private CandidateProfile candidateProfile;

    @Lob
    @Size(max = 5000)
    @Column(nullable = false)
    private String professionalSummary;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> education;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CvLanguage> cvLanguages;

    @ManyToMany
    @JoinTable(
            name = "cv_skills",
            joinColumns = @JoinColumn(name = "cv_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

    @OneToMany(mappedBy = "cv")
    private List<Application> applications;
}
