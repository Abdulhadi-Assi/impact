package com.uni.impact.user.dto;

import com.uni.impact.application.ApplicationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class UserApplicationsResponse {
    private long total;
    private long pending;
    private long approved;
    private long rejected;
    private long withdrawn;
    private long cancelled;
    private Page<ApplicationResponseDTO> applications;
}
