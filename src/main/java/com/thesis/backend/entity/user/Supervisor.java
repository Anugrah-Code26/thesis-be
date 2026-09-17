package com.thesis.backend.entity.user;

import com.thesis.backend.entity.thesis.Thesis;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "supervisors", schema = "thesis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisor_id_gen")
    @SequenceGenerator(name = "supervisor_id_gen", sequenceName = "supervisor_id_seq", schema = "thesis", allocationSize = 1)
    @Column(name = "supervisor_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thesis_id")
    private Thesis thesis;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supervisor that)) return false;

        if (id != null && that.id != null) {
            return id.equals(that.id);
        }

        return lecturer != null && thesis != null &&
                Objects.equals(lecturer.getId(), that.lecturer.getId()) &&
                Objects.equals(thesis.getId(), that.thesis.getId());
    }

    @Override
    public int hashCode() {
        return id != null
                ? id.hashCode()
                : Objects.hash(
                lecturer != null ? lecturer.getId() : 0,
                thesis != null ? thesis.getId() : 0
        );
    }
}
