package com.uni.impact.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {

    @Size(max = 50)
    private String studentNumber;

    @Size(max = 50)
    private String keycloakId;

    @NotNull
    @Size(max = 100)
    private String firstName;

    @NotNull
    @Size(max = 100)
    private String lastName;

    @NotNull
    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 20, min = 10)
    private String phone;

    private Integer academicYear;

    @Size(max = 500)
    private String photo;

    @JsonProperty("isBanned")
    private Boolean isBanned;

    private LocalDate birthdate;

    @Size(max = 255)
    private String location;

    private Long collegeId;
}

