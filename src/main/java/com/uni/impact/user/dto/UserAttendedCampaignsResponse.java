package com.uni.impact.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class UserAttendedCampaignsResponse {
    private long totalCampaigns;
    private double totalHours;
    private Page<UserAttendedCampaignDTO> campaigns;
}
