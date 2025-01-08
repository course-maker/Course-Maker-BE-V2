package net.coursemaker.backendv2.course.command.domain.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "코스 추가 DTO")
@Data
public class AddCourseRequestDTO {

	@Schema(description = "코스 타이틀", example = "Course Title1")
	@NotBlank(message = "코스 타이틀을 입력해야 합니다.")
	private String title;

	@Schema(description = "코스 내용", example = "Course Content1")
	@NotBlank(message = "코스 내용을 입력해야 합니다.")
	private String content;

	@Schema(description = "여행 기간", example = "3")
	@NotNull(message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
	@Min(value = 1, message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
	@Max(value = 3, message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
	private Integer duration;

	@Schema(description = "여행 인원", example = "5")
	@NotNull(message = "여행 인원은 1명 이상이어야 합니다.")
	@Min(value = 1, message = "여행 인원은 1명 이상이어야 합니다.")
	private Integer recommendedTravelerRange;

	@Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course1.jpg")
	@NotBlank(message = "이미지 링크를 넣어야 합니다.")
	private String pictureLink;

	@Schema(description = "코스 여행지 목록")
	@NotNull(message = "최소한 한 개의 코스 여행지가 있어야 합니다.")
	@Size(min = 1, message = "최소한 한 개의 코스 여행지가 있어야 합니다.")
	private List<@Valid AddCourseDestinationRequest> courseDestinations;

	@Schema(description = "유저 닉네임", example = "nickname1", hidden = true)
	private String nickname;

	@Schema(description = "평균 평점", example = "4.5", hidden = true)
	private Double averageRating;

	// @Schema(description = "코스 태그 목록")
	// @NotNull(message = "최소한 한 개의 태그가 있어야 합니다.")
	// @Size(min = 1, message = "최소한 한 개의 태그가 있어야 합니다.")
	// private List<@Valid TagResponseDto> tags;
}
