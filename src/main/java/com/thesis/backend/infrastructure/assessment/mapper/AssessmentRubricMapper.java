package com.thesis.backend.infrastructure.assessment.mapper;

import com.thesis.backend.entity.assessment.AssessmentRubric;
import com.thesis.backend.entity.assessment.AssessmentRubricItem;
import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.entity.assessment.ScoreDetail;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentRubricDTO;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentRubricItemDTO;
import com.thesis.backend.infrastructure.assessment.dto.ScoreDetailDTO;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;

import java.util.*;
import java.util.stream.Collectors;

public class AssessmentRubricMapper {

    public static AssessmentRubric toEntity(AssessmentRubricDTO dto) {
        AssessmentRubric assessmentRubric = new AssessmentRubric();
        assessmentRubric.setName(dto.getName());
        assessmentRubric.setDescription(dto.getDescription());
        assessmentRubric.setAssessmentRole(dto.getAssessmentRole());

        if (dto.getAssessmentRubricItems() != null) {
            assessmentRubric.setAssessmentRubricItems(dto.getAssessmentRubricItems().stream()
                    .map(itemDto -> toAssessmentRubricItemEntity(itemDto, assessmentRubric))
                    .collect(Collectors.toSet()));
        }

        return assessmentRubric;
    }

    private static AssessmentRubricItem toAssessmentRubricItemEntity(AssessmentRubricItemDTO dto, AssessmentRubric parent) {
        AssessmentRubricItem item = new AssessmentRubricItem();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setMaxScore(dto.getMaxScore());
        item.setAssessmentRubric(parent); // set parent

        if (dto.getScoreDetails() != null) {
            item.setScoreDetails(dto.getScoreDetails().stream()
                    .map(scoreDto -> toScoreDetailEntity(scoreDto, item))
                    .collect(Collectors.toSet()));
        }

        return item;
    }

    private static ScoreDetail toScoreDetailEntity(ScoreDetailDTO dto, AssessmentRubricItem parent) {
        ScoreDetail detail = new ScoreDetail();
        detail.setId(dto.getId());
        detail.setScore(dto.getScore());
        detail.setLabel(dto.getLabel());
        detail.setDescription(dto.getDescription());
        detail.setAssessmentRubricItem(parent); // set parent
        return detail;
    }

    public static AssessmentRubricDTO toDTO(AssessmentRubric assessmentRubric) {
        AssessmentRubricDTO dto = new AssessmentRubricDTO();
        dto.setId(assessmentRubric.getId());
        dto.setName(assessmentRubric.getName());
        dto.setDescription(assessmentRubric.getDescription());
        dto.setAssessmentRole(assessmentRubric.getAssessmentRole());

        dto.setExam(toExamDTO(assessmentRubric.getExam()));
        dto.setAssessmentRubricItems(assessmentRubric.getAssessmentRubricItems().stream()
                .map(AssessmentRubricMapper::toAssessmentRubricItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static void updateEntityFromDTO(AssessmentRubric existing, AssessmentRubricDTO dto) {
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setAssessmentRole(dto.getAssessmentRole());

        // Clear and repopulate items
        existing.getAssessmentRubricItems().clear();

        if (dto.getAssessmentRubricItems() != null) {
            dto.getAssessmentRubricItems().forEach(itemDto -> {
                var item = toAssessmentRubricItemEntity(itemDto, existing);
                existing.getAssessmentRubricItems().add(item);
            });
        }
    }

    public static void softUpdateEntityFromDTO(AssessmentRubric existing, AssessmentRubricDTO dto) {
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setAssessmentRole(dto.getAssessmentRole());

        Map<Long, AssessmentRubricItem> existingItemsById = existing.getAssessmentRubricItems().stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(AssessmentRubricItem::getId, item -> item));

        Set<AssessmentRubricItem> updatedItems = new HashSet<>();

        for (AssessmentRubricItemDTO itemDto : dto.getAssessmentRubricItems()) {
            if (itemDto.getId() != null && existingItemsById.containsKey(itemDto.getId())) {
                // Update existing item
                AssessmentRubricItem existingItem = existingItemsById.get(itemDto.getId());
                updateItemFromDTO(existingItem, itemDto);
                updatedItems.add(existingItem);
            } else {
                // Add new item
                AssessmentRubricItem newItem = toAssessmentRubricItemEntity(itemDto, existing);
                updatedItems.add(newItem);
            }
        }

        // Remove items not present in the updated list
        existing.getAssessmentRubricItems().removeIf(item ->
                item.getId() != null && updatedItems.stream().noneMatch(i -> Objects.equals(i.getId(), item.getId()))
        );

        // Add or keep updated items
        updatedItems.forEach(item -> item.setAssessmentRubric(existing));
        existing.setAssessmentRubricItems(updatedItems);
    }

    private static void updateItemFromDTO(AssessmentRubricItem item, AssessmentRubricItemDTO dto) {
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setMaxScore(dto.getMaxScore());

        if (dto.getScoreDetails() != null) {
            if (item.getScoreDetails() == null) {
                item.setScoreDetails(new HashSet<>());
            }
            updateScoreDetailsFromDTO(item, dto.getScoreDetails());
        } else {
            item.getScoreDetails().clear();
        }
    }

    private static void updateScoreDetailsFromDTO(
            AssessmentRubricItem item,
            Collection<ScoreDetailDTO> dtoDetails
    ) {
        Map<Long, ScoreDetail> existingDetailsById = item.getScoreDetails().stream()
                .filter(detail -> detail.getId() != null)
                .collect(Collectors.toMap(ScoreDetail::getId, detail -> detail));

        Set<ScoreDetail> updatedDetails = new HashSet<>();

        for (ScoreDetailDTO dto : dtoDetails) {
            if (dto.getId() != null && existingDetailsById.containsKey(dto.getId())) {
                // Update existing
                ScoreDetail existingDetail = existingDetailsById.get(dto.getId());
                existingDetail.setScore(dto.getScore());
                existingDetail.setLabel(dto.getLabel());
                existingDetail.setDescription(dto.getDescription());
                updatedDetails.add(existingDetail);
            } else {
                // Add new
                ScoreDetail newDetail = toScoreDetailEntity(dto, item);
                updatedDetails.add(newDetail);
            }
        }

        // Remove deleted
        item.getScoreDetails().removeIf(detail ->
                detail.getId() != null && updatedDetails.stream().noneMatch(d -> Objects.equals(d.getId(), detail.getId()))
        );

        item.setScoreDetails(updatedDetails);
    }

    private static ExamDTO toExamDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setName(exam.getName());
        dto.setDescription(exam.getDescription());
        return dto;
    }

    private static AssessmentRubricItemDTO toAssessmentRubricItemDTO(AssessmentRubricItem assessmentRubricItem) {
        AssessmentRubricItemDTO dto = new AssessmentRubricItemDTO();
        dto.setId(assessmentRubricItem.getId());
        dto.setName(assessmentRubricItem.getName());
        dto.setDescription(assessmentRubricItem.getDescription());
        dto.setMaxScore(assessmentRubricItem.getMaxScore());
        dto.setScoreDetails(assessmentRubricItem.getScoreDetails().stream()
                .map(AssessmentRubricMapper::toScoreDetailDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private static ScoreDetailDTO toScoreDetailDTO(ScoreDetail scoreDetail) {
        ScoreDetailDTO dto = new ScoreDetailDTO();
        dto.setId(scoreDetail.getId());
        dto.setScore(scoreDetail.getScore());
        dto.setLabel(scoreDetail.getLabel());
        dto.setDescription(scoreDetail.getDescription());
        return dto;
    }
}
