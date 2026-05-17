package com.uni.impact.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
	Page<Application> findByStudentUserId(Long userId, Pageable pageable);
	Page<Application> findByStudentUserIdAndStatus(Long userId, ApplicationStatus status, Pageable pageable);



	@Query("""
		SELECT a.status AS status, COUNT(a) AS count
		FROM Application a
		WHERE a.student.userId = :userId
		GROUP BY a.status
		""")
	java.util.List<StatusCountProjection> countByStudentGroupedByStatus(@Param("userId") Long userId);

	interface StatusCountProjection {
		ApplicationStatus getStatus();
		Long getCount();
	}
}
