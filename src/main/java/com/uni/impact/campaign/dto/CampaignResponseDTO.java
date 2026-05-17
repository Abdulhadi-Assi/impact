package com.uni.impact.campaign.dto;

import com.uni.impact.progress.ProgressResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CampaignResponseDTO {

    private Long campaignId;
    private String title;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxVolunteers;
    private String status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String proposedByName;
    private String approvedByName;
    private String managedByName;
    private String category;

    private String collegeName;

    private ProgressResponseDTO lastProgress;

    private String photo;
    private List<String> photos;
    private List<String> progressPhotos;
}
