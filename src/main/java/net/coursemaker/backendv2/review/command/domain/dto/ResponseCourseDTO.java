package net.coursemaker.backendv2.review.command.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReview;

@Data
public class ResponseCourseDTO {

	@Schema(description = "리뷰 ID", example = "1")
	private Long reviewId;

	@Schema(description = "리뷰 제목", example = "Amazing Course")
	private String title;

	@Schema(description = "리뷰 설명", example = "이 코스는 정말 멋졌어요! 경치가 아름답고, 음식도 맛있었습니다.")
	private String description;

	@Schema(description = "평점", example = "4.5")
	private Double rating;

	@Schema(description = "리뷰 사진 URL 목록", example = "['url1', 'url2']")
	private List<String> pictures;

	@Schema(description = "작성자 ID", example = "123")
	private Long memberId;

	@Schema(description = "추천 수", example = "10")
	private Integer recommendCount;

	@Schema(description = "리뷰 작성 날짜", example = "2024-09-14")
	private String reviewedAt;

	public static ResponseCourseDTO fromEntity(CourseReview review) {
		ResponseCourseDTO dto = new ResponseCourseDTO();
		dto.setReviewId(review.getId());
		dto.setTitle(review.getTitle());
		dto.setDescription(review.getDescription());
		dto.setRating(review.getRating());
		dto.setPictures(review.getPictures());
		dto.setMemberId(review.getMemberId());
		dto.setRecommendCount(review.getRecommendCount());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		dto.setReviewedAt(review.getCreatedAt().format(formatter));
		return dto;
	}
}
