package net.coursemaker.backendv2.review.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;
import lombok.Getter;

@Getter
public class DuplicateReviewException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public DuplicateReviewException(String clientMessage, String logMessage) {
		super(ReviewErrorCode.DUPLICATE_REVIEW, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
