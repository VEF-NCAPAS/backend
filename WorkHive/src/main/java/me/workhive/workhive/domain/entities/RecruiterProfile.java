package me.workhive.workhive.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recruiters")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruiterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrivateComment> comments;
}
