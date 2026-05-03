package com.uni.impact.campaign;

import com.uni.impact.application.ApplicationDTO;
import com.uni.impact.application.ApplicationMapper;
import com.uni.impact.application.ApplicationService;
import com.uni.impact.attendance.AttendanceDTO;
import com.uni.impact.attendance.AttendanceMapper;
import com.uni.impact.attendance.AttendanceService;
import com.uni.impact.campaign_photo.CampaignPhotoDTO;
import com.uni.impact.campaign_photo.CampaignPhotoMapper;
import com.uni.impact.campaign_photo.CampaignPhotoService;
import com.uni.impact.progress.ProgressDTO;
import com.uni.impact.progress.ProgressMapper;
import com.uni.impact.progress.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignActionsController {

    private final ApplicationService applicationService;
    private final ApplicationMapper applicationMapper;
    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;
    private final ProgressService progressService;
    private final ProgressMapper progressMapper;
    private final CampaignPhotoService campaignPhotoService;
    private final CampaignPhotoMapper campaignPhotoMapper;

    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> apply(@PathVariable Long id, @RequestBody(required = false) ApplicationDTO applicationDTO,
                                      @AuthenticationPrincipal Jwt jwt) {
        String email = jwt == null ? null : jwt.getClaimAsString("email");
        applicationService.applyToCampaign(id, email, applicationDTO);
        return ResponseEntity.ok().build();
    }

    // GET /api/v1/campaigns/{id}/applications
    @GetMapping("/{id}/applications")
    public ResponseEntity<Page<ApplicationDTO>> getApplications(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(applicationService.findByCampaign(id, pageable).map(applicationMapper::toDto));
    }

    @PostMapping("/{id}/attendance")
    public ResponseEntity<Void> createAttendance(@PathVariable Long id, @RequestBody AttendanceDTO attendanceDTO) {
        attendanceDTO.setCampaign(id);
        attendanceService.create(attendanceDTO);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/{id}/attendance/bulk")
//    public ResponseEntity<Void> createAttendanceBulk(@PathVariable Long id, @RequestBody List<AttendanceDTO> attendanceList) {
//        attendanceList.forEach(a -> a.setCampaign(id));
//        attendanceService.createBulk(attendanceList);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/{id}/attendance")
    public ResponseEntity<Page<AttendanceDTO>> getAttendance(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(attendanceService.findByCampaign(id, pageable).map(attendanceMapper::toDto));
    }

    // Progress endpoints
    @PostMapping("/{id}/progress")
    public ResponseEntity<Void> createProgress(@PathVariable Long id, @RequestBody ProgressDTO progressDTO) {
        progressDTO.setCampaign(id);
        progressService.create(progressDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<Page<ProgressDTO>> getProgress(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(progressService.findByCampaign(id, pageable).map(progressMapper::toDto));
    }

    // Photos endpoints
    @PostMapping("/{id}/photos")
    public ResponseEntity<Void> createPhoto(@PathVariable Long id, @RequestBody CampaignPhotoDTO photoDTO) {
        photoDTO.setCampaign(id);
        campaignPhotoService.create(photoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<Page<CampaignPhotoDTO>> getPhotos(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(campaignPhotoService.findByCampaign(id, pageable).map(campaignPhotoMapper::toDto));
    }

}


