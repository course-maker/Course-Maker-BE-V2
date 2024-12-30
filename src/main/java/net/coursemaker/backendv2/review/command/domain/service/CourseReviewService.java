package net.coursemaker.backendv2.review.command.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReview;
import net.coursemaker.backendv2.review.command.domain.dto.RequestCourseDTO;
import net.coursemaker.backendv2.review.command.domain.dto.ResponseCourseDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseReviewService {

	private final CourseReviewDomainService courseReviewDomainService;

	@Transactional
	public CourseReview createReview(RequestCourseDTO request, Long memberId, Long courseId) {
		log.info("[CourseReviewService] 리뷰 생성 - 회원 ID: {}, 강좌 ID: {}", memberId, courseId);
		return courseReviewDomainService.createReview(request, memberId, courseId);
	}

	@Transactional
	public CourseReview updateReview(Long reviewId, RequestCourseDTO request, Long memberId) {
		log.info("[CourseReviewService] 리뷰 수정 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		return courseReviewDomainService.updateReview(reviewId, request, memberId);
	}

	@Transactional
	public void deleteReview(Long reviewId, Long memberId) {
		log.info("[CourseReviewService] 리뷰 삭제 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		courseReviewDomainService.deleteReview(reviewId, memberId);
	}

	public ResponseCourseDTO getReview(Long reviewId) {
		log.info("[CourseReviewService] 리뷰 조회 - 리뷰 ID: {}", reviewId);
		CourseReview review = courseReviewDomainService.findById(reviewId);
		return ResponseCourseDTO.fromEntity(review);
	}

	public List<ResponseCourseDTO> getReviewsByMember(Long memberId, Pageable pageable) {
		log.info("[CourseReviewService] 회원별 리뷰 조회 - 회원 ID: {}", memberId);
		Page<CourseReview> reviews = courseReviewDomainService.findReviewsByMember(memberId, pageable);
		return reviews.stream().map(ResponseCourseDTO::fromEntity).collect(Collectors.toList());
	}

	public void addRecommendation(Long reviewId, Long memberId) {
		log.info("[CourseReviewService] 추천 추가 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		courseReviewDomainService.addRecommendation(reviewId, memberId);
	}

	public void removeRecommendation(Long reviewId, Long memberId) {
		log.info("[CourseReviewService] 추천 삭제 - 리뷰 ID: {}, 회원 ID: {}", reviewId, memberId);
		courseReviewDomainService.removeRecommendation(reviewId, memberId);
	}
}
