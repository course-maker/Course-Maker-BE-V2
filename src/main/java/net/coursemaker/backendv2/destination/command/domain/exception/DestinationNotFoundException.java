package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class DestinationNotFoundException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public DestinationNotFoundException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.DESTINATION_NOT_FOUND, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
