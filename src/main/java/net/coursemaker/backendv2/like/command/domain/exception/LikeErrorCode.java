package net.coursemaker.backendv2.like.command.domain.exception;

import org.springframework.http.HttpStatus;

import net.coursemaker.backendv2.common.ErrorCode;

import lombok.Getter;

@Getter
public enum LikeErrorCode implements ErrorCode {

	DUPLICATE_LIKE("LIKE-001", "duplicate like", HttpStatus.CONFLICT);

	private final String code;
	private final String reasonPhrase;
	private final HttpStatus httpStatus;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	LikeErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
