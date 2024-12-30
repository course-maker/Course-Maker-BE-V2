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
import net.coursemaker.backendv2.review.command.domain.dto.RequestDestinationDTO;
import net.coursemaker.backendv2.review.command.domain.dto.ResponseDestinationDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinationReviewService {

	private final DestinationReviewDomainService destinationReviewDomainService;

	@Transactional
	public DestinationReview createReview(RequestDestinationDTO request, Long memberId, Long destinationId) {
		log.info("[DestinationReviewService] 리뷰 생성 - 회원 ID: {}, 목적지 ID: {}", memberId, destinationId);
		return destinationReviewDomainService.createReview(request, memberId, destinationId);
	}

	@Transactional
	public DestinationReview updateReview(Long reviewId, RequestDestinationDTO request, Long memberId) {
		log.info("[DestinationReviewService] 리뷰 수정 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		return destinationReviewDomainService.updateReview(reviewId, request, memberId);
	}

	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		log.info("[DestinationReviewService] 리뷰 삭제 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		destinationReviewDomainService.deleteReview(reviewId, memberId);
	}

	public ResponseDestinationDTO getReview(Long reviewId) {
		log.info("[DestinationReviewService] 리뷰 조회 - 리뷰 ID: {}", reviewId);
		DestinationReview review = destinationReviewDomainService.findById(reviewId);
		return ResponseDestinationDTO.fromEntity(review);
	}

	public List<ResponseDestinationDTO> getReviewsByMember(Long memberId, Pageable pageable) {
		log.info("[DestinationReviewService] 회원별 리뷰 조회 - 회원 ID: {}", memberId);
		Page<DestinationReview> reviews = destinationReviewDomainService.findReviewsByMember(memberId, pageable);
		return reviews.stream().map(ResponseDestinationDTO::fromEntity).collect(Collectors.toList());
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		log.info("[DestinationReviewService] 추천 추가 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		destinationReviewDomainService.addRecommendation(reviewId, memberId);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		log.info("[DestinationReviewService] 추천 삭제 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		destinationReviewDomainService.removeRecommendation(reviewId, memberId);
	}
}
