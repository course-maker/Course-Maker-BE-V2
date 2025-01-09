package net.coursemaker.backendv2.review.command.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;

@Data
public class RequestDestinationDTO {

	@Schema(description = "리뷰 제목", example = "Great Place")
	@NotNull(message = "Title is required")
	@NotBlank(message = "Title cannot be blank")
	private String title;

	@Schema(description = "리뷰 설명", example = "This is a great destination.")
	@NotNull(message = "Description is required")
	@NotBlank(message = "Description cannot be blank")
	private String description;

	@Schema(description = "평점", example = "4.5")
	@NotNull(message = "Rating is required")
	private Double rating;

	@Schema(description = "리뷰 사진 URL 목록", example = "['url1', 'url2']")
	private List<String> pictures;

	public DestinationReview toEntity(Long memberId, Long destinationId) {
		return new DestinationReview(
			this.title,
			this.description,
			memberId,
			destinationId,
			this.rating,
			this.pictures != null ? this.pictures : List.of()
		);
	}
	public RequestDestinationDTO(String title, String description, Double rating, List<String> pictures) {
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.pictures = pictures;
	}
}
