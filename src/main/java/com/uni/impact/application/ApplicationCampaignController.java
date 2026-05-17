package com.uni.impact.application;

import com.uni.impact.application.dto.VolunteerSearchCriteria;
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
public class ApplicationCampaignController {

    private final ApplicationService applicationService;

    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> apply(@PathVariable Long id, @RequestBody(required = false) ApplicationDTO applicationDTO,
                                      @AuthenticationPrincipal Jwt jwt) {
        String email = jwt == null ? null : jwt.getClaimAsString("email");
        applicationService.applyToCampaign(id, email, applicationDTO);
        return ResponseEntity.ok().build();
    }

    // GET /api/v1/campaigns/{id}/applications
    //   ?status=APPROVED          (filter by application status)
    //   &searchText=ahmed         (search in student first/last name or email)
    //   &collegeId=3              (filter by student's college)
    //   &page=0&size=20           (pagination + sort supported via Pageable)
    @GetMapping("/{id}/applications")
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplications(
            @PathVariable Long id,
            @ModelAttribute VolunteerSearchCriteria criteria,
            Pageable pageable) {
        criteria.setCampaignId(id);
        return ResponseEntity.ok(applicationService.searchVolunteersAsResponse(criteria, pageable));
    }

    // GET /api/v1/campaigns/{id}/students
    //   Student-centric view of everyone who applied to this campaign.
    //   ?status=APPROVED          (filter by application status)
    //   &searchText=ahmed         (search in student first/last name or email)
    //   &collegeId=3              (filter by student's college)
    //   &page=0&size=20           (pagination + sort)
    @GetMapping("/{id}/students")
    public ResponseEntity<Page<CampaignStudentDTO>> getStudents(
            @PathVariable Long id,
            @ModelAttribute VolunteerSearchCriteria criteria,
            Pageable pageable) {
        criteria.setCampaignId(id);
        return ResponseEntity.ok(applicationService.searchCampaignStudents(criteria, pageable));
    }
}

