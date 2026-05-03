package com.uni.impact.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
	Page<Application> findByCampaignCampaignId(Long campaignId, Pageable pageable);
	List<Application> findByStudentUserId(Long userId);
	Page<Application> findByStudentUserId(Long userId, Pageable pageable);
}
