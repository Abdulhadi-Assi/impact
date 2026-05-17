package com.uni.impact.progress;
import com.uni.impact.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProgressMapper {

    @Mapping(target = "campaign", source = "campaign.campaignId")
    @Mapping(target = "updatedBy", source = "updatedBy.userId")
    ProgressDTO toDto(Progress entity);

    @Mapping(target = "campaignTitle", source = "campaign.title")
    @Mapping(target = "updatedByName", source = "updatedBy", qualifiedByName = "fullName")
    ProgressResponseDTO toResponseDto(Progress entity);

    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "progressCampaignPhotos", ignore = true)
    Progress toEntity(ProgressDTO dto);

    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "progressCampaignPhotos", ignore = true)
    void updateEntity(@MappingTarget Progress entity, ProgressDTO dto);

    @Named("fullName")
    default String fullName(User user) {
        if (user == null) return null;
        String first = user.getFirstName() == null ? "" : user.getFirstName();
        String last = user.getLastName() == null ? "" : user.getLastName();
        String full = (first + " " + last).trim();
        return full.isEmpty() ? null : full;
    }
}
