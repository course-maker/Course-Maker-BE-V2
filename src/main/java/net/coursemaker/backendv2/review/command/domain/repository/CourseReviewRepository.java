package net.coursemaker.backendv2.review.command.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReview;
import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReviewRecommendation;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

	// 특정 리뷰에 대한 추천 조회
	Optional<CourseReviewRecommendation> findRecommendation(Long reviewId, Long memberId);

	// 특정 멤버가 작성한 리뷰 조회 (페이징 처리)
	Page<CourseReview> findByMemberId(Long memberId, Pageable pageable);
}
