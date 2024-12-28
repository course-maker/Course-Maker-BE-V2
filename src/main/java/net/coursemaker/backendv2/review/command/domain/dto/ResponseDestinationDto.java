package net.coursemaker.backendv2.review.command.domain.dto;

import lombok.Data;

import java.util.List;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;

@Data
public class ResponseDestinationDto {

	private Long reviewId;
	private String title;
	private String description;
	private Double rating;
	private List<String> pictures;
	private Long memberId;

	public static ResponseDestinationDto fromEntity(DestinationReview review) {
		ResponseDestinationDto dto = new ResponseDestinationDto();
		dto.setReviewId(review.getId());
		dto.setTitle(review.getTitle());
		dto.setDescription(review.getDescription());
		dto.setRating(review.getRating());
		dto.setPictures(review.getPictures());
		dto.setMemberId(review.getMemberId());
		return dto;
	}
}
