package net.coursemaker.backendv2.review.command.domain.exception;

import lombok.Getter;

@Getter
public class MissingRequiredFieldException extends RuntimeException {
	private final String fieldName;

	public MissingRequiredFieldException(String fieldName) {
		super(fieldName + " 필드는 필수 입력 항목입니다.");
		this.fieldName = fieldName;
	}
}
