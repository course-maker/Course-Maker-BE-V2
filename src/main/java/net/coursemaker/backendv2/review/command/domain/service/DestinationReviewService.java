package net.coursemaker.backendv2.review.command.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;
import net.coursemaker.backendv2.review.command.domain.dto.RequestDestinationDto;
import net.coursemaker.backendv2.review.command.domain.dto.ResponseDestinationDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinationReviewService {

	private final DestinationReviewDomainService destinationReviewDomainService;

	@Transactional
	public DestinationReview createReview(RequestDestinationDto request, Long memberId, Long destinationId) {
		log.info("[DestinationReviewService] Create Review - Member ID: {}, Destination ID: {}", memberId, destinationId);
		return destinationReviewDomainService.createReview(request, memberId, destinationId);
	}

	@Transactional
	public DestinationReview updateReview(Long reviewId, RequestDestinationDto request, Long memberId) {
		log.info("[DestinationReviewService] Update Review - Review ID: {}, Member ID: {}", reviewId, memberId);
		return destinationReviewDomainService.updateReview(reviewId, request, memberId);
	}

	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		log.info("[DestinationReviewService] Delete Review - Review ID: {}, Member ID: {}", reviewId, memberId);
		destinationReviewDomainService.deleteReview(reviewId, memberId);
	}

	public ResponseDestinationDto getReview(Long reviewId) {
		log.info("[DestinationReviewService] Get Review - Review ID: {}", reviewId);
		DestinationReview review = destinationReviewDomainService.findById(reviewId);
		return ResponseDestinationDto.fromEntity(review);
	}

	public List<ResponseDestinationDto> getReviewsByMember(Long memberId, Pageable pageable) {
		log.info("[DestinationReviewService] Get Reviews by Member - Member ID: {}", memberId);
		Page<DestinationReview> reviews = destinationReviewDomainService.findReviewsByMember(memberId, pageable);
		return reviews.stream().map(ResponseDestinationDto::fromEntity).collect(Collectors.toList());
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		log.info("[DestinationReviewService] Add Recommendation - Review ID: {}, Member ID: {}", reviewId, memberId);
		destinationReviewDomainService.addRecommendation(reviewId, memberId);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		log.info("[DestinationReviewService] Remove Recommendation - Review ID: {}, Member ID: {}", reviewId, memberId);
		destinationReviewDomainService.removeRecommendation(reviewId, memberId);
	}
}

