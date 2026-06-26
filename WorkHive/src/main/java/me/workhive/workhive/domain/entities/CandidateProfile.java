package me.workhive.workhive.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "candidates")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @OneToOne(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cv cv;
}
