package com.thesis.backend.entity.assessment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssessmentRole {
    LECTURER("Lecturer"),
    SUPERVISOR("Supervisor"),
    EXAMINER("Examiner");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static AssessmentRole fromString(String value) {
        // try match by name first
        for (AssessmentRole t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown assessment role: " + value);
    }
}
