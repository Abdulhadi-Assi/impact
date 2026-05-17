package com.uni.impact.user;
import com.uni.impact.user.dto.UserRequestDTO;
import com.uni.impact.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "collegeName", source = "college.name")
    UserResponseDTO toResponseDto(User entity);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "college", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "isBanned", defaultExpression = "java(Boolean.FALSE)")
    @Mapping(target = "proposedByCampaigns", ignore = true)
    @Mapping(target = "approvedByCampaigns", ignore = true)
    @Mapping(target = "managedByCampaigns", ignore = true)
    @Mapping(target = "updatedByProgresses", ignore = true)
    @Mapping(target = "studentApplications", ignore = true)
    @Mapping(target = "reviewedByApplications", ignore = true)
    @Mapping(target = "removedByApplications", ignore = true)
    @Mapping(target = "studentAttendances", ignore = true)
    @Mapping(target = "recordedByAttendances", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "college", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "proposedByCampaigns", ignore = true)
    @Mapping(target = "approvedByCampaigns", ignore = true)
    @Mapping(target = "managedByCampaigns", ignore = true)
    @Mapping(target = "updatedByProgresses", ignore = true)
    @Mapping(target = "studentApplications", ignore = true)
    @Mapping(target = "reviewedByApplications", ignore = true)
    @Mapping(target = "removedByApplications", ignore = true)
    @Mapping(target = "studentAttendances", ignore = true)
    @Mapping(target = "recordedByAttendances", ignore = true)
    void updateEntity(@MappingTarget User entity, UserRequestDTO dto);
}
