package net.coursemaker.backendv2.course.command.domain.exception;

import org.springframework.http.HttpStatus;

import net.coursemaker.backendv2.common.ErrorCode;

import lombok.Getter;

@Getter
public enum CourseErrorCode implements ErrorCode {

	ILLEGAL_COURSE_ARGUMENT("COURSE-001", "Illegal argument", HttpStatus.BAD_REQUEST),
	INVALID_COURSE("COURSE-001", "Invalid item", HttpStatus.NOT_FOUND);


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

	CourseErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
