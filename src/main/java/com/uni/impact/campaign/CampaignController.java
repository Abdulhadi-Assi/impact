package com.uni.impact.campaign;

import com.uni.impact.campaign.dto.CampaignRequestDTO;
import com.uni.impact.campaign.dto.CampaignResponseDTO;
import com.uni.impact.campaign.dto.CampaignSearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<Page<CampaignResponseDTO>> findAll(@ModelAttribute CampaignSearchCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(campaignService.searchCampaigns(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.findDtoById(id));
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDTO> create(@Valid @RequestBody CampaignRequestDTO campaignDTO) {
        CampaignResponseDTO created = campaignService.create(campaignDTO);
        return ResponseEntity.created(URI.create("/api/v1/campaigns/" + created.getCampaignId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponseDTO> update(@PathVariable Long id, @RequestBody @Valid final CampaignRequestDTO campaignDTO) {
        return ResponseEntity.ok(campaignService.update(id, campaignDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CampaignResponseDTO> patchDetails(@PathVariable Long id, @RequestBody final CampaignRequestDTO campaignDTO) {
        return ResponseEntity.ok(campaignService.patchDetails(id, campaignDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CampaignResponseDTO> updateStatus(@PathVariable Long id, @RequestBody final CampaignStatusUpdateDTO statusDTO) {
        return ResponseEntity.ok(campaignService.updateStatus(id, statusDTO.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        campaignService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
