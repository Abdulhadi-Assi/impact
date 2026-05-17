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

    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String studentPhoto;
    private String studentNumber;
    private Integer studentAcademicYear;
    private String studentCollegeName;

    private String campaignTitle;
    private String reviewedByName;
    private String removedByName;
}
