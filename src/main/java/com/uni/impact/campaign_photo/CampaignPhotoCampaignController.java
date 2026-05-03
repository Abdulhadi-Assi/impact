package com.uni.impact.campaign_photo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignPhotoCampaignController {

    private final CampaignPhotoService campaignPhotoService;
    private final CampaignPhotoMapper campaignPhotoMapper;

    @PostMapping("/{id}/photos")
    public ResponseEntity<CampaignPhotoDTO> createPhoto(@PathVariable Long id, @RequestBody CampaignPhotoDTO photoDTO) {
        photoDTO.setCampaign(id);
        CampaignPhoto created = campaignPhotoService.create(photoDTO);
        return ResponseEntity.created(URI.create("/api/v1/campaign-photos/" + created.getPhotoId()))
                .body(campaignPhotoMapper.toDto(created));
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<Page<CampaignPhotoDTO>> getPhotos(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(campaignPhotoService.findByCampaign(id, pageable).map(campaignPhotoMapper::toDto));
    }
}

