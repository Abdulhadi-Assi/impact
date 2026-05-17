package com.uni.impact.campaign;

import com.uni.impact.campaign.dto.CampaignRequestDTO;
import com.uni.impact.campaign.dto.CampaignResponseDTO;
import com.uni.impact.campaign_photo.CampaignPhoto;
import com.uni.impact.progress.Progress;
import com.uni.impact.progress.ProgressResponseDTO;
import com.uni.impact.user.User;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignMapper {

    @Mapping(target = "proposedByName", source = "proposedBy", qualifiedByName = "fullName")
    @Mapping(target = "approvedByName", source = "approvedBy", qualifiedByName = "fullName")
    @Mapping(target = "managedByName", source = "managedBy", qualifiedByName = "fullName")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "collegeName", source = "proposedBy.college.name")
    @Mapping(target = "lastProgress", source = "campaignProgresses")
    @Mapping(target = "photos", source = "campaignCampaignPhotos")
    @Mapping(target = "progressPhotos", source = "campaignProgresses")
    CampaignResponseDTO toResponseDto(Campaign entity);

    @Mapping(target = "campaignId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "proposedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "managedBy", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "campaignApplications", ignore = true)
    @Mapping(target = "campaignCampaignPhotos", ignore = true)
    @Mapping(target = "campaignAttendances", ignore = true)
    @Mapping(target = "campaignProgresses", ignore = true)
    @Mapping(target = "photo", ignore = true)
    Campaign toEntity(CampaignRequestDTO dto);

    @Mapping(target = "campaignId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "proposedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "managedBy", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "campaignApplications", ignore = true)
    @Mapping(target = "campaignCampaignPhotos", ignore = true)
    @Mapping(target = "campaignAttendances", ignore = true)
    @Mapping(target = "campaignProgresses", ignore = true)
    @Mapping(target = "photo", ignore = true)
    void updateEntity(@MappingTarget Campaign entity, CampaignRequestDTO dto);

    @Mapping(target = "campaignTitle", source = "campaign.title")
    @Mapping(target = "updatedByName", source = "updatedBy", qualifiedByName = "fullName")
    ProgressResponseDTO toProgressDto(Progress progress);

    @Named("fullName")
    default String fullName(User user) {
        if (user == null) return null;
        String first = user.getFirstName() == null ? "" : user.getFirstName();
        String last = user.getLastName() == null ? "" : user.getLastName();
        String full = (first + " " + last).trim();
        return full.isEmpty() ? null : full;
    }

    default ProgressResponseDTO toLastProgress(Set<Progress> progresses) {
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
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());

        return urls.isEmpty() ? null : urls;
    }

    default List<String> toProfilePhotos(Set<CampaignPhoto> photos) {
        if (photos == null || photos.isEmpty()) {
            return null;
        }
        List<String> urls = photos.stream()
                .filter(p -> p.getProgress() == null)
                .sorted(Comparator.comparing(CampaignPhoto::getUploadedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(CampaignPhoto::getPhotoUrl)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        return urls.isEmpty() ? null : urls;
    }
}
