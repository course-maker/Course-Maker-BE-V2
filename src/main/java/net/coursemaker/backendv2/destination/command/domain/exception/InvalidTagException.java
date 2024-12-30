package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class InvalidTagException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public InvalidTagException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.INVALID_TAG, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
