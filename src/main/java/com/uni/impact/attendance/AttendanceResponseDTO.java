package com.uni.impact.attendance;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceResponseDTO {

    private Long attendanceId;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private Double hoursThatDay;
    private String notes;
    private LocalDateTime recordedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String studentName;
    private String studentEmail;
    private String studentCollegeName;
    private String campaignTitle;
    private String recordedByName;
}
