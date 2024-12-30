package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public UnauthorizedAccessException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.UNAUTHORIZED_ACCESS, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
