package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class InvalidLocationException extends RootException {
	private final String clientMessage;

	public InvalidLocationException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.INVALID_LOCATION, logMessage);
		this.clientMessage = clientMessage;
	}
}
