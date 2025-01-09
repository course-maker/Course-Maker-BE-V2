package net.coursemaker.backendv2.review.command.domain.service;


import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReview;
import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReviewRecommendation;
import net.coursemaker.backendv2.review.command.domain.dto.RequestCourseDTO;
import net.coursemaker.backendv2.review.command.domain.exception.DuplicateReviewException;
import net.coursemaker.backendv2.review.command.domain.exception.MissingRequiredFieldException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewAlreadyRecommendedException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewErrorCode;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewNotFoundException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewPermissionDeniedException;
import net.coursemaker.backendv2.review.command.domain.repository.CourseReviewRepository;

@Service
@RequiredArgsConstructor
public class CourseReviewDomainService {

	private final CourseReviewRepository courseReviewRepository;

	@Transactional
	public CourseReview createReview(RequestCourseDTO request, Long memberId, Long courseId) {
		validateRequest(request);
		validateDuplicateReviewIsExist(memberId, courseId);

		CourseReview review = request.toEntity(memberId, courseId);
		return courseReviewRepository.save(review);
	}

	@Transactional
	public CourseReview updateReview(Long reviewId, RequestCourseDTO request, Long memberId) {
		validateRequest(request);
		validateMemberId(memberId);
		validateReviewId(reviewId);

		CourseReview review = findById(reviewId);

		validateReviewOwnership(review, memberId);

		review.update(request.getTitle(), request.getDescription(), request.getRating(), request.getPictures());
		return courseReviewRepository.save(review);
	}

	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		validateMemberId(memberId);
		validateReviewId(reviewId);

		CourseReview review = findById(reviewId);

		validateReviewOwnership(review, memberId);

		review.markAsDeleted();
		courseReviewRepository.save(review);
	}

	public CourseReview findById(Long reviewId) {
		validateReviewId(reviewId);

		return courseReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "리뷰 ID: " + reviewId));
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		validateMemberId(memberId);
		validateReviewId(reviewId);

		CourseReview review = findById(reviewId);
		validateReviewRecommendationIsExist(reviewId, memberId);

		review.addRecommendation(memberId);
		courseReviewRepository.save(review);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		validateMemberId(memberId);
		validateReviewId(reviewId);

		CourseReview review = findById(reviewId);
		review.removeRecommendation(memberId);
		courseReviewRepository.save(review);
	}

	public Page<CourseReview> findReviewsByMember(Long memberId, Pageable pageable) {
		validateMemberId(memberId);

		return courseReviewRepository.findByMemberId(memberId, pageable);
	}

	// 유효성 검증 메서드

	private void validateDuplicateReviewIsExist(Long memberId, Long courseId) {
		validateMemberId(memberId);
		validateCourseId(courseId);

		boolean exists = courseReviewRepository.existsByMemberIdAndCourseId(memberId, courseId);
		if (exists) {
			throw new DuplicateReviewException(
				ReviewErrorCode.DUPLICATE_REVIEW.getReasonPhrase(),
				"중복 리뷰 감지: 회원 ID " + memberId + ", 코스 ID " + courseId
			);
		}
	}

	private void validateReviewRecommendationIsExist(Long reviewId, Long memberId) {
		Optional<CourseReviewRecommendation> existingRecommendation = courseReviewRepository.findRecommendation(reviewId, memberId);
		if (existingRecommendation.isPresent()) {
			throw new ReviewAlreadyRecommendedException(
				"이미 추천한 리뷰입니다.",
				"리뷰 ID: " + reviewId + ", 회원 ID: " + memberId
			);
		}
	}
	private void validateRequest(RequestCourseDTO request) {
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

	private void validateMemberId(Long memberId) {
		if (memberId == null || memberId <= 0) {
			throw new MissingRequiredFieldException("회원 ID가 유효하지 않습니다.", "memberId가 null이거나 0보다 작습니다.");
		}
	}

	private void validateCourseId(Long courseId) {
		if (courseId == null || courseId <= 0) {
			throw new MissingRequiredFieldException("코스 ID가 유효하지 않습니다.", "courseId가 null이거나 0보다 작습니다.");
		}
	}

	private void validateReviewId(Long reviewId) {
		if (reviewId == null || reviewId <= 0) {
			throw new MissingRequiredFieldException("리뷰 ID가 유효하지 않습니다.", "ReviewId 검증 실패");
		}
	}

	private void validateReviewOwnership(CourseReview review, Long memberId) {
		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException(
				ReviewErrorCode.REVIEW_PERMISSION_DENIED.getReasonPhrase(),
				"리뷰 ID: " + review.getId() + ", 회원 ID: " + memberId
			);
		}
	}
}

