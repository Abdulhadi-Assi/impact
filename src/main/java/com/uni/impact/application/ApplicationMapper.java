package com.uni.impact.application;

import com.uni.impact.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationMapper {

    @Mapping(target = "student", source = "student.userId")
    @Mapping(target = "campaign", source = "campaign.campaignId")
    @Mapping(target = "reviewedBy", source = "reviewedBy.userId")
    @Mapping(target = "removedBy", source = "removedBy.userId")
    ApplicationDTO toDto(Application entity);

    @Mapping(target = "studentName", source = "student", qualifiedByName = "fullName")
    @Mapping(target = "studentEmail", source = "student.email")
    @Mapping(target = "studentPhone", source = "student.phone")
    @Mapping(target = "studentPhoto", source = "student.photo")
    @Mapping(target = "studentNumber", source = "student.studentNumber")
    @Mapping(target = "studentAcademicYear", source = "student.academicYear")
    @Mapping(target = "studentCollegeName", source = "student.college.name")
    @Mapping(target = "campaignTitle", source = "campaign.title")
    @Mapping(target = "reviewedByName", source = "reviewedBy", qualifiedByName = "fullName")
    @Mapping(target = "removedByName", source = "removedBy", qualifiedByName = "fullName")
    ApplicationResponseDTO toResponseDto(Application entity);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "removedBy", ignore = true)
    Application toEntity(ApplicationDTO dto);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "removedBy", ignore = true)
    void updateEntity(@MappingTarget Application entity, ApplicationDTO dto);

    @Named("fullName")
    default String fullName(User user) {
        if (user == null) return null;
        String first = user.getFirstName() == null ? "" : user.getFirstName();
        String last = user.getLastName() == null ? "" : user.getLastName();
        String full = (first + " " + last).trim();
        return full.isEmpty() ? null : full;
    }
}
