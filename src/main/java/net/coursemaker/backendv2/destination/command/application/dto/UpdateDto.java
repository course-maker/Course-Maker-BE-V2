package net.coursemaker.backendv2.destination.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

@Data
public class UpdateDto {
	@Schema(description = "유저 닉네임", example = "coursemaker", hidden = true)
	private String nickname; // 유저 이름

	@Schema(description = "여행지 이름", defaultValue = "역시 부산은 해운대!")
	@NotNull(message = "여행지 이름을 입력하세요.")
	@NotBlank(message = "여행지 이름은 공백 혹은 빈 문자는 허용하지 않습니다.")
	private String name; // 여행지 이름

	@Schema(description = "태그 리스트")
	@NotNull(message = "태그 리스트는 비어 있을 수 없습니다.")
	@Size(min = 1, message = "적어도 하나의 태그가 있어야 합니다.")
	private List<@Valid TagResponseDto> tags; // 태그 리스트

	@Schema(description = "위치 정보")
	@NotNull(message = "위치 정보는 비어 있을 수 없습니다.")
	@Valid
	private LocationDto location; // 위치 정보

	@Schema(description = "대표 사진", defaultValue = "http://example.com/coursemaker.jpg")
	@NotNull(message = "대표 사진 링크를 입력하세요.")
	@NotBlank(message = "대표 사진 링크는 공백 혹은 빈 문자는 허용하지 않습니다.")
	private String pictureLink; // 대표 사진

	@Schema(description = "텍스트 에디터", defaultValue = "해운대 물이 깨끗하고, 친구들과 여행하기 너무 좋았어요!")
	private String content; // 텍스트 에디터

	@Schema(description = "평균 평점", example = "4.5", hidden = true)
	private Double averageRating; // 평균 평점

	@Schema(description = "무장애 여행지 여부", nullable = true, hidden = true)
	private Boolean disabled;

	// tourApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.
	@Schema(description = "Tour Api에서 불러온 공공데이터 여행지일 경우 그 여행지에 해당하는 고유 Content ID 값 입니다. tourApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.", nullable = true, hidden = true)
	private Long contentId;

	// busanApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.
	@Schema(description ="busanApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.", nullable = true, hidden = true)
	private Integer seq;

	public void toUpdate(Destination destination, Member member) {
		// 여행지 작성자를 업데이트
		destination.setMember(member);

		// 여행지 이름 업데이트
		destination.setName(this.name);

		// 대표 사진이 null이거나 빈 값이면 기본 링크 설정
		if (this.pictureLink == null || this.pictureLink.isEmpty() || this.pictureLink.isBlank()) {
			destination.setPictureLink("https://i.ibb.co/XsNmR3Q/url-null.jpg");
		} else {
			destination.setPictureLink(this.pictureLink);
		}

		// 여행지 내용, 위치 정보, 평점 등 업데이트
		destination.setContent(this.content);
		destination.setLocation(this.location.getAddress());
		destination.setLongitude(this.location.getLongitude());
		destination.setLatitude(this.location.getLatitude());
		destination.setAverageRating(this.averageRating != null ? this.averageRating : 0.0);

		// 무장애 여행지 여부 업데이트 (null이면 기본값 false 설정)
		destination.setDisabled(this.disabled != null ? this.disabled : false);

		// API 관련 정보 업데이트
		destination.setContentId(this.contentId);
		destination.setSeq(this.seq);
	}

}
