package net.coursemaker.backendv2.common.exception;

import org.springframework.http.HttpStatus;

import net.coursemaker.backendv2.common.ErrorCode;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

	DATA_FORMAT_MISMATCH("COMMON-001", "data format mismatch", HttpStatus.BAD_REQUEST);

	private final String code; // 오류 코드
	private final String reasonPhrase; // 왜 발생했는가
	private final HttpStatus httpStatus; // HTTP 상태코드

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	CommonErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
