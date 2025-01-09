package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class InvalidContentException extends RootException {
	private final String clientMessage;

	public InvalidContentException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.INVALID_CONTENT, logMessage);
		this.clientMessage = clientMessage;
	}
}

