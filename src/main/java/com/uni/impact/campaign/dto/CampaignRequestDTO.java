package com.uni.impact.campaign.dto;

import com.uni.impact.campaign.CampaignStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

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

    @Schema(type = "string", format = "binary", description = "Campaign profile photo file (single)")
    private MultipartFile photoFile;

    @ArraySchema(schema = @Schema(type = "string", format = "binary"),
            arraySchema = @Schema(description = "Multiple campaign profile photo files"))
    private MultipartFile[] photoFiles;

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
