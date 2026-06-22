package me.workhive.workhive.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.workhive.workhive.domain.entities.enums.LanguageLevel;

import java.util.UUID;

@Entity
@Table(name = "cv_languages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CvLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageLevel level;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
