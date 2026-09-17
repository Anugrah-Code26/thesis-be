package com.thesis.backend.entity.thesis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamType {
    EXAM_ONE("Exam 1"),
    EXAM_TWO("Exam 2"),
    EXAM_THREE("Exam 3");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static ExamType fromString(String value) {
        // try match by name first
        for (ExamType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown exam type: " + value);
    }
}
