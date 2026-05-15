package com.uni.impact.campaign;

import com.uni.impact.campaign.dto.CampaignRequestDTO;
import com.uni.impact.campaign.dto.CampaignResponseDTO;
import com.uni.impact.campaign.dto.CampaignSearchCriteria;
import com.uni.impact.category.Category;
import com.uni.impact.category.CategoryRepository;
import com.uni.impact.user.User;
import com.uni.impact.user.UserRepository;
import com.uni.impact.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CampaignMapper campaignMapper;

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public Page<CampaignResponseDTO> findAll(Pageable pageable) {
        return campaignRepository.findAll(pageable).map(campaignMapper::toResponseDto);
    }


    @Transactional(readOnly = true)
    public Page<CampaignResponseDTO> searchCampaigns(CampaignSearchCriteria criteria, Pageable pageable) {
        Specification<Campaign> spec = CampaignSpecification.withSearchCriteria(criteria);
        return campaignRepository.findAll(spec, pageable).map(campaignMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Campaign findById(final Long campaignId) {
        return campaignRepository.findById(campaignId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public CampaignResponseDTO findDtoById(final Long campaignId) {
        return campaignMapper.toResponseDto(findById(campaignId));
    }

    @Transactional
    public CampaignResponseDTO patchDetails(final Long campaignId, final CampaignRequestDTO campaignDTO) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(NotFoundException::new);
        campaignMapper.updateEntity(campaign, campaignDTO);
        return campaignMapper.toResponseDto(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponseDTO updateStatus(final Long campaignId, final CampaignStatus newStatus) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(NotFoundException::new);
        CampaignStatus current = campaign.getStatus();
        if (!isValidTransition(current, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + current + " to " + newStatus);
        }
        campaign.setStatus(newStatus);
        return campaignMapper.toResponseDto(campaignRepository.save(campaign));
    }

    private boolean isValidTransition(final CampaignStatus from, final CampaignStatus to) {
        if (from == to) return true;
        return switch (from) {
            case PENDING ->
                    to == CampaignStatus.APPROVED || to == CampaignStatus.REJECTED || to == CampaignStatus.CANCELED;
            case APPROVED -> to == CampaignStatus.ONGOING || to == CampaignStatus.CANCELED;
            case ONGOING -> to == CampaignStatus.COMPLETED || to == CampaignStatus.CANCELED;
            default -> false; // REJECTED, COMPLETED, CANCELED are terminal
        };
    }

    @Transactional
    public CampaignResponseDTO create(final CampaignRequestDTO campaignDTO) {
        Campaign campaign = campaignMapper.toEntity(campaignDTO);
        applyPhoto(campaign, campaignDTO);
        applyRelations(campaign, campaignDTO);
        return campaignMapper.toResponseDto(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponseDTO update(final Long campaignId, final CampaignRequestDTO campaignDTO) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(NotFoundException::new);
        campaignMapper.updateEntity(campaign, campaignDTO);
        applyPhoto(campaign, campaignDTO);
        applyRelations(campaign, campaignDTO);
        return campaignMapper.toResponseDto(campaignRepository.save(campaign));
    }

    public void delete(final Long campaignId) {
        final Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(NotFoundException::new);
        try {
                campaignRepository.delete(campaign);
             } catch (final Exception e) {
                throw new IllegalStateException("campaign could not be deleted");
        }
    }


    private void applyRelations(final Campaign campaign, final CampaignRequestDTO campaignDTO) {
        final User proposedBy = campaignDTO.getProposedById() == null ? null : userRepository.findById(campaignDTO.getProposedById())
                .orElseThrow(() -> new NotFoundException("proposedBy not found"));
        campaign.setProposedBy(proposedBy);
        final User approvedBy = campaignDTO.getApprovedById() == null ? null : userRepository.findById(campaignDTO.getApprovedById())
                .orElseThrow(() -> new NotFoundException("approvedBy not found"));
        campaign.setApprovedBy(approvedBy);
        final User managedBy = campaignDTO.getManagedById() == null ? null : userRepository.findById(campaignDTO.getManagedById())
                .orElseThrow(() -> new NotFoundException("managedBy not found"));
        campaign.setManagedBy(managedBy);
        final Category category = campaignDTO.getCategoryId() == null ? null : categoryRepository.findById(campaignDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("category not found"));
        campaign.setCategory(category);
    }

    private void applyPhoto(final Campaign campaign, final CampaignRequestDTO campaignDTO) {
        MultipartFile photoFile = campaignDTO.getPhotoFile();
        if (photoFile != null && !photoFile.isEmpty()) {
            campaign.setPhoto(storePhoto(photoFile));
        }
    }

    private String storePhoto(final MultipartFile photoFile) {
        if (photoFile == null || photoFile.isEmpty()) {
            throw new IllegalArgumentException("Photo file cannot be empty");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String originalName = photoFile.getOriginalFilename() == null
                    ? "campaign-photo"
                    : Paths.get(photoFile.getOriginalFilename()).getFileName().toString();
            String filename = UUID.randomUUID() + "_" + originalName;
            Files.write(uploadPath.resolve(filename), photoFile.getBytes());
            return "/uploads/photos/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload campaign photo", e);
        }
    }
}
