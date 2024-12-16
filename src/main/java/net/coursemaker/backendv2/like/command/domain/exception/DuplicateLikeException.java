package net.coursemaker.backendv2.like.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class DuplicateLikeException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public DuplicateLikeException(String clientMessage, String logMessage) {
		super(LikeErrorCode.DUPLICATE_LIKE, logMessage);

		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
