package com.thesis.backend.entity.thesis;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thesis.backend.entity.assessment.AssessmentScore;
import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.entity.user.Examiner;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "thesis_exams", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThesisExam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thesis_exam_id_gen")
    @SequenceGenerator(name = "thesis_exam_id_gen", sequenceName = "thesis_exam_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @Column(name = "scheduled_date")
    private OffsetDateTime scheduledDate;

    @Column
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ThesisExamStatus status = ThesisExamStatus.REQUESTED;

    // File metadata
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath; // physical path or object key

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "file_size")
    private Long fileSize;

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
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thesis_id", nullable = false)
    private Thesis thesis;

    @JsonManagedReference
    @OneToMany(mappedBy = "thesisExam", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Examiner> examiners = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "thesisExam", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssessmentScore> assessmentScores = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThesisExam thesisExam)) return false;
        return id != null && id.equals(thesisExam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}