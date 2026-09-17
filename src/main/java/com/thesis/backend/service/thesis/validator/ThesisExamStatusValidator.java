package com.thesis.backend.service.thesis.validator;

import com.thesis.backend.entity.thesis.ThesisExamStatus;

public class ThesisExamStatusValidator {

    public static boolean isValidTransition(ThesisExamStatus currentStatus, ThesisExamStatus newStatus) {
        return switch (currentStatus) {
            case SCHEDULED -> newStatus == ThesisExamStatus.CONFIRMED ||
                    newStatus == ThesisExamStatus.CANCELLED;
            case CONFIRMED -> newStatus == ThesisExamStatus.IN_PROGRESS ||
                    newStatus == ThesisExamStatus.CANCELLED ||
                    newStatus == ThesisExamStatus.RESCHEDULED;
            case IN_PROGRESS -> newStatus == ThesisExamStatus.COMPLETED ||
                    newStatus == ThesisExamStatus.CANCELLED;
            case COMPLETED -> false; // Final state
            case CANCELLED, RESCHEDULED -> newStatus == ThesisExamStatus.SCHEDULED;
            default -> false;
        };
    }
}
