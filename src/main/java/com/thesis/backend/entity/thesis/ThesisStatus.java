package com.thesis.backend.entity.thesis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThesisStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    REVISION_REQUIRED("Revision Required"),
    DEFENDED("Defended"),
    ARCHIVED("Archived");

    private final String displayName;
}
