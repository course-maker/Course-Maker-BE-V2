package net.coursemaker.backendv2.auth.token.exception;

import org.springframework.http.HttpStatus;

import net.coursemaker.backendv2.common.ErrorCode;


public enum TokenErrorCode implements ErrorCode {

	INVALID_TOKEN("TOKEN-001", "invalid type data", HttpStatus.BAD_REQUEST),
	EXPIRED_ACCESS_TOKEN("TOKEN-002", "expired token", HttpStatus.UNAUTHORIZED),
	EXPIRED_REFRESH_TOKEN("TOKEN-003", "expired token", HttpStatus.UNAUTHORIZED),
	MANIPULATED_TOKEN("TOKEN-004", "manipulated token", HttpStatus.BAD_REQUEST),;

	private final String code; // 오류 코드
	private final String reasonPhrase; // 왜 발생했는가
	private final HttpStatus httpStatus; // HTTP 상태코드

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

	TokenErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
