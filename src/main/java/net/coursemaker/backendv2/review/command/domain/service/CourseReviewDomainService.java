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
		validateMemberId(memberId);
		validateCourseId(courseId);

		boolean exists = courseReviewRepository.existsByMemberIdAndCourseId(memberId, courseId);
		if (exists) {
			throw new DuplicateReviewException(
				ReviewErrorCode.DUPLICATE_REVIEW.getReasonPhrase(),
				"중복 리뷰 감지: 회원 ID " + memberId + ", 코스 ID " + courseId
			);
		}

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

		Optional<CourseReviewRecommendation> existingRecommendation = courseReviewRepository.findRecommendation(reviewId, memberId);
		if (existingRecommendation.isPresent()) {
			throw new ReviewAlreadyRecommendedException("이미 추천한 리뷰입니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

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
	private void validateRequest(RequestCourseDTO request) {
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

	private void validateCourseId(Long courseId) {
		if (courseId == null || courseId <= 0) {
			throw new MissingRequiredFieldException("courseId");
		}
	}

	private void validateReviewId(Long reviewId) {
		if (reviewId == null || reviewId <= 0) {
			throw new MissingRequiredFieldException("reviewId");
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

