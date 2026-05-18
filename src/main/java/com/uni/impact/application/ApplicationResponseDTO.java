package com.uni.impact.application;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationResponseDTO {

    private Long id;

    private String motivationLetter;
    private ApplicationStatus status;
    private String rejectionReason;
    private String adminNotes;

    private LocalDateTime appliedAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reviewedAt;

    private String removalReason;
    private LocalDateTime removedAt;

    private Long campaignId;
    private String campaignTitle;
    private String campaignPhoto;
    private String campaignCategory;
    private String campaignCollegeName;

    private String reviewedByName;
    private String removedByName;
}
