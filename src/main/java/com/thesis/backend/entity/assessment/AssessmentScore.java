package com.thesis.backend.entity.assessment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thesis.backend.entity.thesis.ThesisExam;
import com.thesis.backend.entity.user.Lecturer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "assessment_scores", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessment_score_id_gen")
    @SequenceGenerator(name = "assessment_score_id_gen", sequenceName = "assessment_score_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @Column
    private Integer score;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

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

    // Relationships
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thesis_exam_id", nullable = false)
    private ThesisExam thesisExam;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id", nullable = false)
    private Lecturer evaluator;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_rubric_item_id", nullable = false)
    private AssessmentRubricItem assessmentRubricItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssessmentScore assessmentScore)) return false;
        return id != null && id.equals(assessmentScore.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}