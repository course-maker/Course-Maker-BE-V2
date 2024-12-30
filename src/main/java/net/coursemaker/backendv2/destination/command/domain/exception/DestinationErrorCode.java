package net.coursemaker.backendv2.destination.command.domain.exception;

import org.springframework.http.HttpStatus;
import net.coursemaker.backendv2.common.ErrorCode;
import lombok.Getter;

@Getter
public enum DestinationErrorCode implements ErrorCode {

	DESTINATION_NOT_FOUND("DESTINATION-001", "여행지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	INVALID_NAME("DESTINATION-002", "유효하지 않은 여행지 이름입니다.", HttpStatus.BAD_REQUEST),
	INVALID_LOCATION("DESTINATION-003", "유효하지 않은 여행지 위치입니다.", HttpStatus.BAD_REQUEST),
	INVALID_PICTURE_LINK("DESTINATION-004", "유효하지 않은 대표 사진 링크입니다.", HttpStatus.BAD_REQUEST),
	INVALID_CONTENT("DESTINATION-005", "유효하지 않은 여행지 설명입니다.", HttpStatus.BAD_REQUEST),
	INVALID_TAG("DESTINATION-006", "유효하지 않은 태그입니다.", HttpStatus.BAD_REQUEST),
	INVALID_RATING("DESTINATION-007", "유효하지 않은 리뷰 평점입니다.", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED_ACCESS("DESTINATION-008", "수정 권한이 없습니다.", HttpStatus.FORBIDDEN);


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

