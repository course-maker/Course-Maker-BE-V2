package net.coursemaker.backendv2.review.command.domain.exception;

import org.springframework.http.HttpStatus;
import net.coursemaker.backendv2.common.ErrorCode;
import lombok.Getter;

@Getter
public enum ReviewErrorCode implements ErrorCode {

	REVIEW_NOT_FOUND("REVIEW-001", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	REVIEW_PERMISSION_DENIED("REVIEW-002", "리뷰 수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
	REVIEW_ALREADY_RECOMMENDED("REVIEW-003", "이미 추천한 리뷰입니다.", HttpStatus.BAD_REQUEST);

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

	ReviewErrorCode(final String code, final String reasonPhrase, HttpStatus httpStatus) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		this.httpStatus = httpStatus;
	}
}
