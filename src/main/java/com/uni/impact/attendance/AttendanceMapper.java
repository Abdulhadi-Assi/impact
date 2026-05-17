package com.uni.impact.attendance;

import com.uni.impact.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttendanceMapper {

    @Mapping(target = "student", source = "student.userId")
    @Mapping(target = "campaign", source = "campaign.campaignId")
    @Mapping(target = "recordedBy", source = "recordedBy.userId")
    AttendanceDTO toDto(Attendance entity);

    @Mapping(target = "studentName", source = "student", qualifiedByName = "fullName")
    @Mapping(target = "studentEmail", source = "student.email")
    @Mapping(target = "studentCollegeName", source = "student.college.name")
    @Mapping(target = "campaignTitle", source = "campaign.title")
    @Mapping(target = "recordedByName", source = "recordedBy", qualifiedByName = "fullName")
    AttendanceResponseDTO toResponseDto(Attendance entity);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    Attendance toEntity(AttendanceDTO dto);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    void updateEntity(@MappingTarget Attendance entity, AttendanceDTO dto);

    @Named("fullName")
    default String fullName(User user) {
        if (user == null) return null;
        String first = user.getFirstName() == null ? "" : user.getFirstName();
        String last = user.getLastName() == null ? "" : user.getLastName();
        String full = (first + " " + last).trim();
        return full.isEmpty() ? null : full;
    }
}
