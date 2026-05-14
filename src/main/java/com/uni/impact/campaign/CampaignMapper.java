package com.uni.impact.campaign;
import com.uni.impact.campaign.dto.CampaignRequestDTO;
import com.uni.impact.campaign.dto.CampaignResponseDTO;
import com.uni.impact.campaign_photo.CampaignPhoto;
import com.uni.impact.progress.Progress;
import com.uni.impact.progress.ProgressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignMapper {

    @Mapping(target = "proposedById", source = "proposedBy.userId")
    @Mapping(target = "approvedById", source = "approvedBy.userId")
    @Mapping(target = "managedById", source = "managedBy.userId")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "collegeId", source = "proposedBy.college.collegeId")
    @Mapping(target = "collegeName", source = "proposedBy.college.name")
    @Mapping(target = "lastProgress", source = "campaignProgresses")
    @Mapping(target = "progressPhotos", source = "campaignProgresses")
    CampaignResponseDTO toResponseDto(Campaign entity);

    @Mapping(target = "proposedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "managedBy", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "campaignApplications", ignore = true)
    @Mapping(target = "campaignCampaignPhotos", ignore = true)
    @Mapping(target = "campaignAttendances", ignore = true)
    @Mapping(target = "campaignProgresses", ignore = true)
    Campaign toEntity(CampaignRequestDTO dto);

    @Mapping(target = "proposedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "managedBy", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "campaignApplications", ignore = true)
    @Mapping(target = "campaignCampaignPhotos", ignore = true)
    @Mapping(target = "campaignAttendances", ignore = true)
    @Mapping(target = "campaignProgresses", ignore = true)
    void updateEntity(@MappingTarget Campaign entity, CampaignRequestDTO dto);

    @Mapping(target = "campaign", source = "campaign.campaignId")
    @Mapping(target = "updatedBy", source = "updatedBy.userId")
    ProgressDTO toProgressDto(Progress progress);

    default ProgressDTO toLastProgress(Set<Progress> progresses) {
        if (progresses == null || progresses.isEmpty()) {
            return null;
        }

        Comparator<Progress> comparator = Comparator
                .comparing(Progress::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Progress::getProgressId, Comparator.nullsLast(Comparator.naturalOrder()));

        return progresses.stream()
                .max(comparator)
                .map(this::toProgressDto)
                .orElse(null);
    }

    default List<String> toProgressPhotos(Set<Progress> progresses) {
        if (progresses == null || progresses.isEmpty()) {
            return null;
        }

        List<String> urls = progresses.stream()
                .sorted(Comparator.comparing(Progress::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .flatMap(progress -> progress.getProgressCampaignPhotos().stream()
                        .sorted(Comparator.comparing(CampaignPhoto::getUploadedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(CampaignPhoto::getPhotoUrl))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));

        return urls.isEmpty() ? null : urls;
    }
}
