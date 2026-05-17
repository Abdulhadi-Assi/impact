package com.uni.impact.attendance;

import com.uni.impact.campaign.CampaignStatus;
import com.uni.impact.user.dto.UserAttendedCampaignDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Page<Attendance> findByCampaignCampaignId(Long campaignId, Pageable pageable);
    List<Attendance> findByStudentUserId(Long userId);

    @Query("""
        SELECT new com.uni.impact.user.dto.UserAttendedCampaignDTO(
            a.campaign.campaignId,
            a.campaign.title,
            a.campaign.description,
            a.campaign.location,
            CAST(a.campaign.status AS string),
            a.campaign.category.name,
            CONCAT(a.campaign.proposedBy.firstName, ' ', a.campaign.proposedBy.lastName),
            a.campaign.proposedBy.college.name,
            a.campaign.photo,
            a.campaign.startDate,
            a.campaign.endDate,
            COUNT(a),
            COALESCE(SUM(a.hoursThatDay), 0.0)
        )
        FROM Attendance a
        WHERE a.student.userId = :userId
          AND (:search = '' OR LOWER(a.campaign.title) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR a.campaign.status = :status)
        GROUP BY a.campaign.campaignId,
                 a.campaign.title,
                 a.campaign.description,
                 a.campaign.location,
                 a.campaign.status,
                 a.campaign.category.name,
                 a.campaign.proposedBy.firstName,
                 a.campaign.proposedBy.lastName,
                 a.campaign.proposedBy.college.name,
                 a.campaign.photo,
                 a.campaign.startDate,
                 a.campaign.endDate
        """)
    Page<UserAttendedCampaignDTO> findAttendedCampaignsByUser(
            @Param("userId") Long userId,
            @Param("search") String search,
            @Param("status") CampaignStatus status,
            Pageable pageable);

    @Query("""
        SELECT COUNT(DISTINCT a.campaign.campaignId)
        FROM Attendance a
        WHERE a.student.userId = :userId
        """)
    long countDistinctCampaignsByUser(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(a.hoursThatDay), 0.0)
        FROM Attendance a
        WHERE a.student.userId = :userId
        """)
    double sumHoursByUser(@Param("userId") Long userId);
}
