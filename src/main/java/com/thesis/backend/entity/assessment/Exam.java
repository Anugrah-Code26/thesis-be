package com.thesis.backend.entity.assessment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thesis.backend.entity.thesis.ThesisExam;
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
@Table(name = "exams", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_id_gen")
    @SequenceGenerator(name = "exam_id_gen", sequenceName = "exam_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @NotNull(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;

    @Column
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

    @JsonManagedReference
    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ThesisExam> thesisExams = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssessmentRubric> assessmentRubrics = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam exam)) return false;
        return id != null && id.equals(exam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
