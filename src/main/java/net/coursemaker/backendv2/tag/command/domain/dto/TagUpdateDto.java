package net.coursemaker.backendv2.tag.command.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TagUpdateDto {

	@Schema(description = "수정하려는 태그의 Id", example = "1")
	@NotNull(message = "태그 ID를 입력하세요.")
	private final Long id;

	@Schema(description = "수정된 태그 이름", example = "연인")
	@NotNull(message = "태그 이름을 입력하세요.")
	@NotBlank(message = "태그 이름은 공백 혹은 빈 문자는 허용하지 않습니다.")
	private final String name;

	@Schema(description = "수정된 태그에 대한 설명", example = "태그에 대한 설명")
	@NotNull(message = "태그에 대한 설명을 입력하세요.")
	@NotBlank(message = "태그에 대한 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
	private final String description;

	public TagUpdateDto(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
}

