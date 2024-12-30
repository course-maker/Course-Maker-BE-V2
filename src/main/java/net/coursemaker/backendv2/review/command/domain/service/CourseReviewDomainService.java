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
import net.coursemaker.backendv2.review.command.domain.exception.ReviewAlreadyRecommendedException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewNotFoundException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewPermissionDeniedException;
import net.coursemaker.backendv2.review.command.domain.repository.CourseReviewRepository;

@Service
@RequiredArgsConstructor
public class CourseReviewDomainService {

	private final CourseReviewRepository courseReviewRepository;

	@Transactional
	public CourseReview createReview(RequestCourseDTO request, Long memberId, Long courseId) {
		CourseReview review = request.toEntity(memberId, courseId);
		return courseReviewRepository.save(review);
	}

	@Transactional
	public CourseReview updateReview(Long reviewId, RequestCourseDTO request, Long memberId) {
		CourseReview review = findById(reviewId);

		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException("리뷰를 수정할 권한이 없습니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.update(request.getTitle(), request.getDescription(), request.getRating(), request.getPictures());
		return courseReviewRepository.save(review);
	}

	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		CourseReview review = findById(reviewId);

		if (!review.getMemberId().equals(memberId)) {
			throw new ReviewPermissionDeniedException("리뷰를 삭제할 권한이 없습니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.markAsDeleted();
		courseReviewRepository.save(review);
	}

	public CourseReview findById(Long reviewId) {
		return courseReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "리뷰 ID: " + reviewId));
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		CourseReview review = findById(reviewId);

		Optional<CourseReviewRecommendation> existingRecommendation = courseReviewRepository.findRecommendation(reviewId, memberId);
		if (existingRecommendation.isPresent()) {
			throw new ReviewAlreadyRecommendedException("이미 추천한 리뷰입니다.", "리뷰 ID: " + reviewId + ", 회원 ID: " + memberId);
		}

		review.addRecommendation(memberId);
		courseReviewRepository.save(review);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		CourseReview review = findById(reviewId);
		review.removeRecommendation(memberId);
		courseReviewRepository.save(review);
	}

	public Page<CourseReview> findReviewsByMember(Long memberId, Pageable pageable) {
		return courseReviewRepository.findByMemberId(memberId, pageable);
	}
}
