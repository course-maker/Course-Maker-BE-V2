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
		validateDuplicateReviewIsExist(memberId, destinationId);

		DestinationReview review = request.toEntity(memberId, destinationId);
		return destinationReviewRepository.save(review);
	}

	@Transactional
	public DestinationReview updateReview(Long reviewId, RequestDestinationDTO request, Long memberId) {
		validateRequest(request);
		validateEntityExistence(reviewId, memberId);

		DestinationReview review = findById(reviewId);
		validateReviewOwnership(review, memberId);

		review.update(request);
		return destinationReviewRepository.save(review);
	}


	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		validateEntityExistence(reviewId, memberId);

		DestinationReview review = findById(reviewId);
		validateReviewOwnership(review, memberId);

		review.markAsDeleted();
		destinationReviewRepository.save(review);
	}

	public DestinationReview findById(Long reviewId) {
		validateReviewId(reviewId);

		return destinationReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "리뷰 ID: " + reviewId));
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		validateEntityExistence(reviewId, memberId);
		validateReviewRecommendationIsExist(reviewId, memberId);

		DestinationReview review = findById(reviewId);
		review.addRecommendation(memberId);
		destinationReviewRepository.save(review);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		validateEntityExistence(reviewId, memberId);

		DestinationReview review = findById(reviewId);
		review.removeRecommendation(memberId);
		destinationReviewRepository.save(review);
	}

	public Page<DestinationReview> findReviewsByMember(Long memberId, Pageable pageable) {
		validateMemberId(memberId, "회원 ID가 유효하지 않습니다.", "MemberId 검증 실패");

		return destinationReviewRepository.findByMemberId(memberId, pageable);
	}

	// 검증 메서드

	private void validateDuplicateReviewIsExist(Long memberId, Long destinationId) {
		validateMemberId(memberId, "회원 ID가 유효하지 않습니다.", "MemberId 검증 실패");
		validateDestinationId(destinationId, "목적지 ID가 유효하지 않습니다.", "DestinationId 검증 실패");

		boolean exists = destinationReviewRepository.existsByMemberIdAndDestinationId(memberId, destinationId);
		if (exists) {
			throw new DuplicateReviewException(
				ReviewErrorCode.DUPLICATE_REVIEW.getReasonPhrase(),
				"중복 리뷰 감지: 회원 ID " + memberId + ", 목적지 ID " + destinationId
			);
		}
	}

	private void validateReviewRecommendationIsExist(Long reviewId, Long memberId) {
		Optional<DestinationReviewRecommendation> existingRecommendation = destinationReviewRepository.findRecommendation(reviewId, memberId);
		if (existingRecommendation.isPresent()) {
			throw new ReviewAlreadyRecommendedException(
				"이미 추천한 리뷰입니다.",
				"리뷰 ID: " + reviewId + ", 회원 ID: " + memberId
			);
		}
	}

	private void validateRequest(RequestDestinationDTO request) {
		if (request == null) {
			throw new MissingRequiredFieldException("요청 데이터가 없습니다.", "Request 데이터 검증 실패");
		}
		if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
			throw new MissingRequiredFieldException("제목이 비어 있습니다.", "Title 검증 실패");
		}
		if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
			throw new MissingRequiredFieldException("설명이 비어 있습니다.", "Description 검증 실패");
		}
		if (request.getRating() == null || request.getRating() < 0 || request.getRating() > 5) {
			throw new MissingRequiredFieldException("평점이 잘못되었습니다.", "Rating 검증 실패");
		}
	}

	private void validateMemberId(Long memberId, String clientMessage, String logMessage) {
		if (memberId == null || memberId <= 0) {
			throw new MissingRequiredFieldException(clientMessage, logMessage);
		}
	}

	private void validateDestinationId(Long destinationId, String clientMessage, String logMessage) {
		if (destinationId == null || destinationId <= 0) {
			throw new MissingRequiredFieldException(clientMessage, logMessage);
		}
	}

	private void validateReviewId(Long reviewId) {
		if (reviewId == null || reviewId <= 0) {
			throw new MissingRequiredFieldException("리뷰 ID가 유효하지 않습니다.", "ReviewId 검증 실패");
		}
	}

	private void validateReviewOwnership(DestinationReview review, Long memberId) {
		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException(
				ReviewErrorCode.REVIEW_PERMISSION_DENIED.getReasonPhrase(),
				"리뷰 ID: " + review.getId() + ", 회원 ID: " + memberId
			);
		}
	}

	private void validateEntityExistence(Long reviewId, Long memberId) {
		validateReviewId(reviewId);
		validateMemberId(memberId, "회원 ID가 유효하지 않습니다.", "MemberId 검증 실패");
	}
}
