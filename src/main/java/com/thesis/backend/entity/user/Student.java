package com.thesis.backend.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thesis.backend.entity.thesis.Thesis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "students", schema = "thesis")
@Getter
@Setter
public class Student extends User {

    @Column(name = "unique_id")
    private String uniqueId;

    @JsonManagedReference
    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private Thesis thesis;

}