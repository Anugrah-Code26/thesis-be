package com.thesis.backend.entity.assessment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "assessment_rubric_items", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRubricItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessment_rubric_item_id_gen")
    @SequenceGenerator(name = "assessment_rubric_item_id_gen", sequenceName = "assessment_rubric_item_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @NotNull(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @NotNull(message = "Max Score is mandatory")
    @Column(nullable = false)
    private Integer maxScore = 5;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    @PreRemove
    protected void onRemove() {
        deletedAt = OffsetDateTime.now();
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_rubric_id", nullable = false)
    private AssessmentRubric assessmentRubric;

    @JsonManagedReference
    @OneToMany(mappedBy = "assessmentRubricItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScoreDetail> scoreDetails = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "assessmentRubricItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssessmentScore> assessmentScores = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssessmentRubricItem assessmentRubricItem)) return false;
        return id != null && id.equals(assessmentRubricItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
