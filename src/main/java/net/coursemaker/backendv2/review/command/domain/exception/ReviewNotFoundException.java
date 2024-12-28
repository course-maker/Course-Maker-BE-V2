package net.coursemaker.backendv2.review.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class ReviewNotFoundException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public ReviewNotFoundException(String clientMessage, String logMessage) {
		super(ReviewErrorCode.REVIEW_NOT_FOUND, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
