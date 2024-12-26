package net.coursemaker.backendv2.review.command.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import net.coursemaker.backendv2.review.command.domain.aggregate.CourseReview;

@Data
public class RequestCourseDto {

	@Schema(description = "리뷰 제목", example = "Amazing Course")
	@NotNull(message = "리뷰 제목을 입력하세요.")
	@NotBlank(message = "리뷰 제목은 공백일 수 없습니다.")
	private String title;

	@Schema(description = "리뷰 설명", example = "이 코스는 정말 멋졌어요! 경치가 아름답고, 음식도 맛있었습니다.")
	@NotNull(message = "리뷰 설명을 입력하세요.")
	@NotBlank(message = "리뷰 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
	private String description;

	@Schema(description = "평점", example = "4.5")
	@NotNull(message = "평점을 입력하세요.")
	private Double rating;

	@Schema(description = "리뷰 사진 URL 목록", example = "['url1', 'url2']")
	private List<String> pictures;

	public CourseReview toEntity(Long memberId, Long courseId) {
		return new CourseReview(
			this.title,
			this.description,
			memberId,
			courseId,
			this.rating,
			this.pictures != null ? this.pictures : List.of()
		);
	}
}
