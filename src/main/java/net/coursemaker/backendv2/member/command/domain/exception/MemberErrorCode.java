package net.coursemaker.backendv2.member.command.domain.exception;

import org.springframework.http.HttpStatus;

import net.coursemaker.backendv2.common.ErrorCode;

import lombok.Getter;

@Getter
public enum MemberErrorCode implements ErrorCode {

	EMAIL_DUPLICATED("USER-001", "duplicated data", HttpStatus.BAD_REQUEST),
	NICKNAME_DUPLICATED("USER-002", "duplicated data", HttpStatus.BAD_REQUEST),
	PHONE_NUMBER_DUPLICATED("USER-003", "duplicated data", HttpStatus.BAD_REQUEST),
	MEMBER_NOT_FOUND("USER-004", "user not found", HttpStatus.NOT_FOUND),
	PASSWORD_NOT_CORRECT("USER-005", "password not correct", HttpStatus.BAD_REQUEST),;

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

	MemberErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
