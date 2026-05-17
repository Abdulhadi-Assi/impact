package com.uni.impact.campaign_photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignPhotoMapper {

    @Mapping(target = "campaign", source = "campaign.campaignId")
    @Mapping(target = "progress", source = "progress.progressId")
    CampaignPhotoDTO toDto(CampaignPhoto entity);

    @Mapping(target = "campaignTitle", source = "campaign.title")
    @Mapping(target = "progressPercentage", source = "progress.percentage")
    CampaignPhotoResponseDTO toResponseDto(CampaignPhoto entity);

    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "progress", ignore = true)
    CampaignPhoto toEntity(CampaignPhotoDTO dto);

    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "progress", ignore = true)
    void updateEntity(@MappingTarget CampaignPhoto entity, CampaignPhotoDTO dto);
}
