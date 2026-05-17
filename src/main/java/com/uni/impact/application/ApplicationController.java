package com.uni.impact.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.uni.impact.user.UserRepository;
import com.uni.impact.application.dto.VolunteerSearchCriteria;


@RestController
@RequestMapping(value = "/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<ApplicationResponseDTO>> findAll(@ModelAttribute VolunteerSearchCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(applicationService.searchVolunteersAsResponse(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.findByIdAsResponse(id));
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> create(@Valid @RequestBody ApplicationDTO applicationDTO) {
        Application created = applicationService.create(applicationDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/applications/" + created.getId()))
                .body(applicationService.findByIdAsResponse(created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid final ApplicationDTO applicationDTO) {
        applicationService.update(id, applicationDTO);
        return ResponseEntity.ok(applicationService.findByIdAsResponse(id));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<ApplicationResponseDTO>> myApplications(
            Pageable pageable,
            @AuthenticationPrincipal Jwt jwt) {
        String email = jwt == null ? null : jwt.getClaimAsString("email");
        return ResponseEntity.ok(applicationService.findByStudentEmailAsResponse(email, pageable));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> changeStatus(@PathVariable Long id, @RequestBody final ApplicationDTO applicationDTO) {
        applicationService.changeStatus(id, applicationDTO.getStatus(), null, applicationDTO.getRejectionReason());
        return ResponseEntity.ok(applicationService.findByIdAsResponse(id));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApplicationResponseDTO> withdraw(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String email = jwt == null ? null : jwt.getClaimAsString("email");
        final com.uni.impact.user.User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new RuntimeException("user not found"));
        applicationService.withdraw(id, user.getUserId());
        return ResponseEntity.ok(applicationService.findByIdAsResponse(id));
    }

    @PatchMapping("/{id}/remove")
    public ResponseEntity<ApplicationResponseDTO> remove(@PathVariable Long id, @RequestBody final ApplicationDTO applicationDTO,
                                                 @AuthenticationPrincipal Jwt jwt) {
        Long removerId = null;
        if (jwt != null) {
            final com.uni.impact.user.User u = userRepository.findByEmailIgnoreCase(jwt.getClaimAsString("email")).orElse(null);
            removerId = u == null ? null : u.getUserId();
        }
        applicationService.remove(id, removerId, applicationDTO.getRemovalReason());
        return ResponseEntity.ok(applicationService.findByIdAsResponse(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable final Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
