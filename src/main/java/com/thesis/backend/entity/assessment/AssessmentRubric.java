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
@Table(name = "assessment_rubrics", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRubric {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessment_rubric_id_gen")
    @SequenceGenerator(name = "assessment_rubric_id_gen", sequenceName = "assessment_rubric_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @NotNull(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssessmentRole assessmentRole;

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
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @JsonManagedReference
    @OneToMany(mappedBy = "assessmentRubric", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssessmentRubricItem> assessmentRubricItems = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssessmentRubric assessmentRubric)) return false;
        return id != null && id.equals(assessmentRubric.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
