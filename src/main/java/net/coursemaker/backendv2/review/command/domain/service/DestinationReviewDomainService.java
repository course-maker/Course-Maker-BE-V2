package net.coursemaker.backendv2.review.command.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;
import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReviewRecommendation;
import net.coursemaker.backendv2.review.command.domain.dto.RequestDestinationDTO;
import net.coursemaker.backendv2.review.command.domain.exception.DuplicateReviewException;
import net.coursemaker.backendv2.review.command.domain.exception.MissingRequiredFieldException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewAlreadyRecommendedException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewErrorCode;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewNotFoundException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewPermissionDeniedException;
import net.coursemaker.backendv2.review.command.domain.repository.DestinationReviewRepository;

@Service
@RequiredArgsConstructor
public class DestinationReviewDomainService {

	private final DestinationReviewRepository destinationReviewRepository;

	@Transactional
	public DestinationReview createReview(RequestDestinationDTO request, Long memberId, Long destinationId) {
		validateRequest(request);
		validateMemberId(memberId);
		validateDestinationId(destinationId);

		boolean exists = destinationReviewRepository.existsByMemberIdAndDestinationId(memberId, destinationId);
		if (exists) {
			throw new DuplicateReviewException(
				ReviewErrorCode.DUPLICATE_REVIEW.getReasonPhrase(),
				"중복 리뷰 감지: 회원 ID " + memberId + ", 목적지 ID " + destinationId
			);
		}
		DestinationReview review = request.toEntity(memberId, destinationId);
		return destinationReviewRepository.save(review);
	}

	@Transactional
	public DestinationReview updateReview(Long reviewId, RequestDestinationDTO request, Long memberId) {
		validateRequest(request);
		validateMemberId(memberId);
		validateReviewId(reviewId);

		DestinationReview review = findById(reviewId);

		validateReviewIsMine(review, memberId);

		review.update(request);
		return destinationReviewRepository.save(review);
	}


	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		validateMemberId(memberId);
		validateReviewId(reviewId);

		DestinationReview review = findById(reviewId);

		validateReviewIsMine(review, memberId);

		review.markAsDeleted();
		destinationReviewRepository.save(review);
	}

	public DestinationReview findById(Long reviewId) {
		validateReviewId(reviewId);

		return destinationReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "리뷰 ID: " + reviewId));
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		validateMemberId(memberId);
		validateReviewId(reviewId);

		DestinationReview review = findById(reviewId);

		Optional<DestinationReviewRecommendation> existingRecommendation = destinationReviewRepository.findRecommendation(reviewId, memberId);
		if (existingRecommendation.isPresent()) {
			throw new ReviewAlreadyRecommendedException("이미 추천한 리뷰입니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.addRecommendation(memberId);
		destinationReviewRepository.save(review);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		validateMemberId(memberId);
		validateReviewId(reviewId);

		DestinationReview review = findById(reviewId);
		review.removeRecommendation(memberId);
		destinationReviewRepository.save(review);
	}

	public Page<DestinationReview> findReviewsByMember(Long memberId, Pageable pageable) {
		validateMemberId(memberId);

		return destinationReviewRepository.findByMemberId(memberId, pageable);
	}

	// 검증 메서드
	private void validateRequest(RequestDestinationDTO request) {
		if (request == null) {
			throw new MissingRequiredFieldException("request");
		}
		if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
			throw new MissingRequiredFieldException("title");
		}
		if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
			throw new MissingRequiredFieldException("description");
		}
		if (request.getRating() == null || request.getRating() < 0 || request.getRating() > 5) {
			throw new MissingRequiredFieldException("rating");
		}
	}

	private void validateMemberId(Long memberId) {
		if (memberId == null || memberId <= 0) {
			throw new MissingRequiredFieldException("memberId");
		}
	}

	private void validateDestinationId(Long destinationId) {
		if (destinationId == null || destinationId <= 0) {
			throw new MissingRequiredFieldException("destinationId");
		}
	}

	private void validateReviewId(Long reviewId) {
		if (reviewId == null || reviewId <= 0) {
			throw new MissingRequiredFieldException("reviewId");
		}
	}

	private void validateReviewIsMine(DestinationReview review, Long memberId) {
		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException(
				ReviewErrorCode.REVIEW_PERMISSION_DENIED.getReasonPhrase(),
				"리뷰 ID: " + review.getId() + ", 회원 ID: " + memberId
			);
		}
	}
}
