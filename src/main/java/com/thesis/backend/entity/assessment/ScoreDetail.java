package com.thesis.backend.entity.assessment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "score_details", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_detail_id_gen")
    @SequenceGenerator(name = "score_detail_id_gen", sequenceName = "score_detail_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @NotNull(message = "Score is mandatory")
    @Column(nullable = false)
    private Integer score;

    @NotNull(message = "Label is mandatory")
    @Column(nullable = false)
    private String label;

    @NotNull(message = "description is mandatory")
    @Column(nullable = false)
    private String description;

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
    @JoinColumn(name = "assessment_rubric_item_id", nullable = false)
    private AssessmentRubricItem assessmentRubricItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScoreDetail scoreDetail)) return false;
        return id != null && id.equals(scoreDetail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
