package net.coursemaker.backendv2.tag.command.domain.exception;

import lombok.Getter;
import net.coursemaker.backendv2.common.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum TagErrorCode implements ErrorCode {
	TAG_NOT_FOUND("TAG-001", "태그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	TAG_DUPLICATED("TAG-002", "이미 존재하는 태그입니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String reasonPhrase;
	private final HttpStatus httpStatus;

	TagErrorCode(String code, String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
