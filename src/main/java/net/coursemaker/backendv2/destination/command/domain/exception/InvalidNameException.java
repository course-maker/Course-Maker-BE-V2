package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class InvalidNameException extends RootException {
	private final String clientMessage;

	public InvalidNameException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.INVALID_NAME, logMessage);
		this.clientMessage = clientMessage;
	}
}
