package net.coursemaker.backendv2.review.command.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;
import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReviewRecommendation;

public interface DestinationReviewRepository extends JpaRepository<DestinationReview, Long> {
	Optional<DestinationReviewRecommendation> findRecommendation(Long reviewId, Long memberId);

	Page<DestinationReview> findByMemberId(Long memberId, Pageable pageable);
}
