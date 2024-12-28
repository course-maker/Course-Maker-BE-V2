package net.coursemaker.backendv2.destination.command.domain.exception;

import org.springframework.http.HttpStatus;
import net.coursemaker.backendv2.common.ErrorCode;
import lombok.Getter;

@Getter
public enum DestinationErrorCode implements ErrorCode {

	DESTINATION_NOT_FOUND("DESTINATION-001", "여행지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

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

	DestinationErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}

