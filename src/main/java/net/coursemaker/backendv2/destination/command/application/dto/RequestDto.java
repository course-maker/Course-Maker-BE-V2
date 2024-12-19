package net.coursemaker.backendv2.destination.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Location;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

@Data
public class RequestDto {
	@Schema(description = "유저 ID", example = "1", hidden = true)
	private Long memberId; // 유저 ID

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

	@Schema(description = "Tour Api에서 불러온 공공데이터 여행지일 경우 그 여행지에 해당하는 고유 Content ID 값 입니다. tourApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.", nullable = true, hidden = true)
	private Long contentId;

	@Schema(description ="busanApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.", nullable = true, hidden = true)
	private Integer seq;

	@Schema(description = "공공데이터 여행지의 설명입니다.", nullable = true, hidden = true)
	private String apiContent;

	// RequestDto를 Destination 엔티티로 변환하는 메서드
	public Destination toEntity(Long memberId) {
		Location locationEntity = new Location(this.location.getAddress(), this.location.getLongitude(), this.location.getLatitude());
		return new Destination(
			memberId,
			this.name,
			this.pictureLink != null && !this.pictureLink.isBlank() ? this.pictureLink : "https://i.ibb.co/XsNmR3Q/url-null.jpg",
			this.content,
			locationEntity,
			this.averageRating != null ? this.averageRating : 0.0,
			this.disabled != null ? this.disabled : false,
			this.contentId,
			this.seq,
			this.apiContent
		);
	}
}
