package com.thesis.backend.entity.thesis;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thesis.backend.entity.user.Student;
import com.thesis.backend.entity.user.Supervisor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "theses", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Thesis {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thesis_id_gen")
    @SequenceGenerator(name = "thesis_id_gen", sequenceName = "thesis_id_seq", schema = "thesis", allocationSize = 1)
    private Long id;

    @NotNull(message = "Title is mandatory")
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThesisStatus status = ThesisStatus.DRAFT;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "submission_date")
    private OffsetDateTime submissionDate;

    @Column(name = "abstract_text")
    private String abstractText;

    @Column
    private String keywords;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @JsonManagedReference
    @OneToMany(mappedBy = "thesis", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Supervisor> supervisors = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "thesis", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ThesisExam> exams = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thesis thesis)) return false;
        return id != null && id.equals(thesis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
