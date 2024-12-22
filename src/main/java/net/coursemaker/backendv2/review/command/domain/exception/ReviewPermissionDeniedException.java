package net.coursemaker.backendv2.review.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class ReviewPermissionDeniedException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public ReviewPermissionDeniedException(String clientMessage, String logMessage) {
		super(ReviewErrorCode.REVIEW_PERMISSION_DENIED, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
