package me.workhive.workhive.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.Cv;

import java.util.UUID;

@Entity
@Data
@Table(name = "education")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String Institution;

    private String major;

    private String description;

    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;
}
