package net.coursemaker.backendv2.review.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;
import lombok.Getter;

@Getter
public class MissingRequiredFieldException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public MissingRequiredFieldException(String clientMessage, String logMessage) {
		super(ReviewErrorCode.MISSING_REQUIRED_FIELD, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
