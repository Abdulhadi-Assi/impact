package com.uni.impact.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {

    private Long userId;
    private String studentNumber;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer academicYear;
    private String photo;
    private String description;

    @JsonProperty("isBanned")
    private Boolean isBanned;

    private LocalDate birthdate;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String collegeName;
}
