package com.uni.impact.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserAttendedCampaignDTO {

    private Long campaignId;
    private String title;
    private String description;
    private String location;
    private String status;
    private String categoryName;
    private String proposedByName;
    private String collegeName;
    private String photo;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long daysAttended;
    private Double totalHours;
}
