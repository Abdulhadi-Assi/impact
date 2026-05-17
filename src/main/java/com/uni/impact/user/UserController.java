package com.uni.impact.user;

import com.uni.impact.application.ApplicationService;
import com.uni.impact.application.ApplicationStatus;
import com.uni.impact.attendance.AttendanceService;
import com.uni.impact.campaign.CampaignStatus;
import com.uni.impact.user.dto.UserApplicationsResponse;
import com.uni.impact.user.dto.UserAttendedCampaignsResponse;
import com.uni.impact.user.dto.UserRequestDTO;
import com.uni.impact.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AttendanceService attendanceService;
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.findDtoByEmail(jwt.getClaimAsString("email")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findDtoById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userDTO) {
        UserResponseDTO created = userService.create(userDTO);
        return ResponseEntity.created(URI.create("/api/v1/users/" + created.getUserId()))
                .body(created);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> createMultipart(
            @Valid @ModelAttribute UserRequestDTO userDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile) {
        userDTO.setPhotoFile(resolvePhotoFile(userDTO.getPhotoFile(), photoFile, photo, file));
        UserResponseDTO created = userService.create(userDTO);
        return ResponseEntity.created(URI.create("/api/v1/users/" + created.getUserId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid final UserRequestDTO userDTO) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> updateMultipart(
            @PathVariable Long id,
            @Valid @ModelAttribute final UserRequestDTO userDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile) {
        userDTO.setPhotoFile(resolvePhotoFile(userDTO.getPhotoFile(), photoFile, photo, file));
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @PatchMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> updatePhoto(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile) {
        return ResponseEntity.ok(userService.updatePhoto(id, resolvePhotoFile(null, photoFile, photo, file)));
    }

    private MultipartFile resolvePhotoFile(MultipartFile... candidates) {
        if (candidates == null) {
            return null;
        }
        for (MultipartFile candidate : candidates) {
            if (candidate != null && !candidate.isEmpty()) {
                return candidate;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<UserResponseDTO> toggleBan(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleBan(id));
    }

    @GetMapping("/{id}/hours")
    public ResponseEntity<Double> getUserHours(@PathVariable Long id) {
        Double hours = attendanceService.getTotalHoursForUser(id);
        return ResponseEntity.ok(hours);
    }

    // GET /api/v1/users/{id}/attended-campaigns
    //   Returns all campaigns the user has attended, with per-campaign days/hours
    //   plus overall totals (totalCampaigns, totalHours).
    //   ?searchText=  search in campaign title
    //   &status=      filter by campaign status (PENDING/APPROVED/ONGOING/COMPLETED/REJECTED)
    //   &page=&size=&sort=
    @GetMapping("/{id}/attended-campaigns")
    public ResponseEntity<UserAttendedCampaignsResponse> getAttendedCampaigns(
            @PathVariable Long id,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) CampaignStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAttendedCampaigns(id, searchText, status, pageable));
    }

    // GET /api/v1/users/{id}/applications
    //   Returns all applications by the user, with status counts (total/pending/approved/rejected/withdrawn/cancelled).
    //   ?status=  filter by application status
    //   &page=&size=&sort=
    @GetMapping("/{id}/applications")
    public ResponseEntity<UserApplicationsResponse> getUserApplications(
            @PathVariable Long id,
            @RequestParam(required = false) ApplicationStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(applicationService.getUserApplications(id, status, pageable));
    }
}
