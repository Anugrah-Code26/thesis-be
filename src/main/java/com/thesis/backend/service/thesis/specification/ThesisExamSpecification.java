package com.thesis.backend.service.thesis.specification;

import com.thesis.backend.entity.thesis.ExamType;
import com.thesis.backend.entity.thesis.ThesisExam;
import com.thesis.backend.entity.thesis.ThesisExamStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ThesisExamSpecification {

    public static Specification<ThesisExam> hasThesisId(Long thesisId) {
        return (root, query, cb) ->
                cb.equal(root.get("thesis").get("id"), thesisId);
    }

    public static Specification<ThesisExam> hasExam(Long examId) {
        return (root, query, cb) -> {
            if (examId == null) {
                return cb.conjunction();
            }

            try {
                return cb.equal(root.get("exam").get("id"), examId);
            } catch (IllegalArgumentException e) {
                return cb.disjunction();
            }
        };
    }

    public static Specification<ThesisExam> hasSupervisor(Long supervisorId) {
        return (root, query, cb) -> {
            if (supervisorId == null) {
                return cb.conjunction();
            }

            // Join ThesisExam -> Thesis -> Supervisors -> Lecturer
            return cb.equal(
                    root.join("thesis").join("supervisors").join("lecturer").get("id"),
                    supervisorId
            );
        };
    }

    public static Specification<ThesisExam> hasScheduledDate(String date) {
        return (root, query, cb) -> {
            if (date == null || date.isEmpty()) {
                return cb.conjunction();
            }

            try {
                // Parse the input date (e.g., "2025-08-07")
                LocalDate localDate = LocalDate.parse(date);

                // Define range: from start of day to end of day in UTC
                OffsetDateTime startOfDay = localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
                OffsetDateTime endOfDay = localDate.atTime(23, 59, 59).atOffset(ZoneOffset.UTC);

                return cb.between(root.get("scheduledDate"), startOfDay, endOfDay);
            } catch (Exception e) {
                // If parsing fails, return no results
                return cb.disjunction();
            }
        };
    }

//    public static Specification<ThesisExam> hasExamType(String type) {
//        return (root, query, cb) -> {
//            if (type == null || type.isEmpty()) {
//                return cb.conjunction();
//            }
//
//            try {
//                return cb.equal(root.get("examType"), ExamType.valueOf(type.toUpperCase()));
//            } catch (IllegalArgumentException e) {
//                return cb.disjunction();
//            }
//        };
//    }

    public static Specification<ThesisExam> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return cb.conjunction();
            }

            try {
                ThesisExamStatus statusEnum = ThesisExamStatus.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), statusEnum);
            } catch (IllegalArgumentException e) {
                return cb.disjunction();
            }
        };
    }
}
