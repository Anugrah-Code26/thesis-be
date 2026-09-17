package com.thesis.backend.entity.thesis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThesisExamStatus {
    REQUESTED("Requested"),
    SCHEDULED("Scheduled"),
    CONFIRMED("Confirmed"),
    IN_PROGRESS("In Progress"),
    PASSED("Passed"),
    FAILED("Failed"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    RESCHEDULED("Rescheduled");

    private final String displayName;
}
