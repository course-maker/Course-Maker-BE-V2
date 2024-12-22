package net.coursemaker.backendv2.review.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;
import lombok.Getter;

@Getter
public class ReviewAlreadyRecommendedException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public ReviewAlreadyRecommendedException(String clientMessage, String logMessage) {
		super(ReviewErrorCode.REVIEW_ALREADY_RECOMMENDED, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
