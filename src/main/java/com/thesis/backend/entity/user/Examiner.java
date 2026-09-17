package com.thesis.backend.entity.user;

import com.thesis.backend.entity.thesis.ThesisExam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "examiners", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Examiner {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examiner_id_gen")
    @SequenceGenerator(name = "examiner_id_gen", sequenceName = "examiner_id_seq", schema = "thesis", allocationSize = 1)
    @Column(name = "examiner_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thesis_exam_id")
    private ThesisExam thesisExam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Examiner that)) return false;

        if (id != null && that.id != null) {
            return id.equals(that.id);
        }

        return lecturer != null && thesisExam != null &&
                Objects.equals(lecturer.getId(), that.lecturer.getId()) &&
                Objects.equals(thesisExam.getId(), that.thesisExam.getId());
    }

    @Override
    public int hashCode() {
        return id != null
                ? id.hashCode()
                : Objects.hash(
                lecturer != null ? lecturer.getId() : 0,
                thesisExam != null ? thesisExam.getId() : 0
        );
    }
}
