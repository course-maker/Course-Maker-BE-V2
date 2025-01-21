package net.coursemaker.backendv2.tag.command.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TagResponseDto {

	@Schema(description = "태그의 Id", example = "1")
	private final Long id;

	@Schema(description = "태그 이름", example = "연인")
	private final String name;

	@Schema(description = "태그에 대한 설명", example = "태그에 대한 설명")
	private final String description;

	public TagResponseDto(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
}
