package com.uni.impact.application;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CampaignStudentDTO {

    private Long userId;
    private String studentNumber;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer academicYear;
    private String photo;
    private String description;
    private LocalDate birthdate;
    private String location;
    private String collegeName;

    private Long applicationId;
    private ApplicationStatus applicationStatus;
    private String motivationLetter;
    private String rejectionReason;
    private String adminNotes;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime removedAt;
    private String removalReason;
    private String reviewedByName;
}
