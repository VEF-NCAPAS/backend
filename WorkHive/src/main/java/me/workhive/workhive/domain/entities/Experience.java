package me.workhive.workhive.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.Cv;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "experience")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String company;

    private String position;

    private String description;

    private LocalDate hireDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;
}
