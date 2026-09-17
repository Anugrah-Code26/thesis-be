package com.thesis.backend.service.thesis.validator;

import com.thesis.backend.entity.thesis.ThesisStatus;

public class ThesisStatusValidator {

    public static boolean isValidTransition(ThesisStatus currentStatus, ThesisStatus newStatus) {
        return switch (currentStatus) {
            case DRAFT, REVISION_REQUIRED -> newStatus == ThesisStatus.SUBMITTED;
            case SUBMITTED -> newStatus == ThesisStatus.APPROVED ||
                    newStatus == ThesisStatus.REJECTED ||
                    newStatus == ThesisStatus.REVISION_REQUIRED;
            case APPROVED -> newStatus == ThesisStatus.DEFENDED;
            case DEFENDED -> newStatus == ThesisStatus.ARCHIVED;
            default -> false;
        };
    }
}
