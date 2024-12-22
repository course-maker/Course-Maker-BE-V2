package net.coursemaker.backendv2.review.command.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;
import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReviewRecommendation;
import net.coursemaker.backendv2.review.command.domain.dto.RequestDestinationDto;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewAlreadyRecommendedException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewNotFoundException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewPermissionDeniedException;
import net.coursemaker.backendv2.review.command.domain.repository.DestinationReviewRepository;

@Service
@RequiredArgsConstructor
public class DestinationReviewDomainService {

	private final DestinationReviewRepository destinationReviewRepository;

	@Transactional
	public DestinationReview createReview(RequestDestinationDto request, Long memberId, Long destinationId) {
		DestinationReview review = request.toEntity(memberId, destinationId);
		return destinationReviewRepository.save(review);
	}

	@Transactional
	public DestinationReview updateReview(Long reviewId, RequestDestinationDto request, Long memberId) {
		DestinationReview review = findById(reviewId);

		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException("리뷰를 수정할 권한이 없습니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.update(request);
		return destinationReviewRepository.save(review);
	}


	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		DestinationReview review = findById(reviewId);

		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException("리뷰를 삭제할 권한이 없습니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.markAsDeleted();
		destinationReviewRepository.save(review);
	}

	public DestinationReview findById(Long reviewId) {
		return destinationReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "리뷰 ID: " + reviewId));
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		DestinationReview review = findById(reviewId);

		Optional<DestinationReviewRecommendation> existingRecommendation = destinationReviewRepository.findRecommendation(reviewId, memberId);
		if (existingRecommendation.isPresent()) {
			throw new ReviewAlreadyRecommendedException("이미 추천한 리뷰입니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.addRecommendation(memberId);
		destinationReviewRepository.save(review);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		DestinationReview review = findById(reviewId);
		review.removeRecommendation(memberId);
		destinationReviewRepository.save(review);
	}

	public Page<DestinationReview> findReviewsByMember(Long memberId, Pageable pageable) {
		return destinationReviewRepository.findByMemberId(memberId, pageable);
	}
}
