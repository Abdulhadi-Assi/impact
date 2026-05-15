package com.uni.impact.campaign.dto;

import com.uni.impact.campaign.CampaignStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CampaignRequestDTO {

    @Size(max = 255)
    @NotNull
    private String title;

    private String description;

    @Size(max = 255)
    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxVolunteers;

    @Size(max = 500)
    private String photo;

    @NotNull
    private CampaignStatus status;

    private LocalDateTime publishedAt;

    @NotNull
    private Long proposedById;

    private Long approvedById;

    private Long managedById;

    @NotNull
    private Long categoryId;
}

