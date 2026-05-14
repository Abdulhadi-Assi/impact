package com.uni.impact.campaign.dto;

import com.uni.impact.progress.ProgressDTO;
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

    private Long proposedById;
    private Long approvedById;
    private Long managedById;
    private String category;

    private Long collegeId;
    private String collegeName;

    private ProgressDTO lastProgress;
    private String photo;  // Campaign profile photo
    private List<String> progressPhotos;  // Photos from all progress updates
}






