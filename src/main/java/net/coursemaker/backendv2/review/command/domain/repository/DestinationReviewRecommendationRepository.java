package net.coursemaker.backendv2.review.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReviewRecommendation;

public interface DestinationReviewRecommendationRepository extends JpaRepository<DestinationReviewRecommendation, Long> {
}

