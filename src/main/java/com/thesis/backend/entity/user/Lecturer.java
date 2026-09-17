package com.thesis.backend.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisExam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "lecturers", schema = "thesis")
@Getter
@Setter
public class Lecturer extends User {

    @Column(name = "unique_id")
    private String uniqueId;

    @JsonBackReference
    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Supervisor> supervisors = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Examiner> examiners = new HashSet<>();

    @JsonIgnore
    @Transient
    public Set<Thesis> getTheses() {
        Set<Thesis> theses = new HashSet<>();
        for (Supervisor supervisor : supervisors) {
            theses.add(supervisor.getThesis());
        }
        return theses;
    }

    public void addThesis(Thesis thesis) {
        boolean alreadySupervising = supervisors.stream()
                .anyMatch(s -> {
                    Thesis t = s.getThesis();

                    // ✅ If both IDs exist, compare by ID
                    if (t.getId() != null && thesis.getId() != null) {
                        return t.getId().equals(thesis.getId());
                    }

                    // ✅ If IDs are not set yet, compare by object reference
                    return t == thesis;
                });

        if (!alreadySupervising) {
            Supervisor supervisor = new Supervisor();
            supervisor.setLecturer(this);
            supervisor.setThesis(thesis);

            supervisors.add(supervisor);
            thesis.getSupervisors().add(supervisor); // Maintain both sides
        }
    }

    public void removeThesis(Thesis thesis) {
        supervisors.stream()
                .filter(s -> s.getThesis().equals(thesis))
                .findFirst()
                .ifPresent(supervisor -> {
                    supervisors.remove(supervisor);
                    thesis.getSupervisors().remove(supervisor);
                    supervisor.setLecturer(null);
                    supervisor.setThesis(null);
                });
    }

    @JsonIgnore
    @Transient
    public Set<ThesisExam> getThesisExams() {
        Set<ThesisExam> thesisExams = new HashSet<>();
        for (Examiner examiner : examiners) {
            thesisExams.add(examiner.getThesisExam());
        }
        return thesisExams;
    }

    public void addThesisExam(ThesisExam thesisExam) {
        boolean alreadySupervising = examiners.stream()
                .anyMatch(s -> {
                    ThesisExam t = s.getThesisExam();

                    // ✅ If both IDs exist, compare by ID
                    if (t.getId() != null && thesisExam.getId() != null) {
                        return t.getId().equals(thesisExam.getId());
                    }

                    // ✅ If IDs are not set yet, compare by object reference
                    return t == thesisExam;
                });

        if (!alreadySupervising) {
            Examiner examiner = new Examiner();
            examiner.setLecturer(this);
            examiner.setThesisExam(thesisExam);

            examiners.add(examiner);
            thesisExam.getExaminers().add(examiner); // Maintain both sides
        }
    }

    public void removeThesisExam(ThesisExam thesis) {
        examiners.stream()
                .filter(s -> s.getThesisExam().equals(thesis))
                .findFirst()
                .ifPresent(examiner -> {
                    examiners.remove(examiner);
                    thesis.getExaminers().remove(examiner);
                    examiner.setLecturer(null);
                    examiner.setThesisExam(null);
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecturer that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
