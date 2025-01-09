package net.coursemaker.backendv2.destination.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class InvalidPictureLinkException extends RootException {
	private final String clientMessage;

	public InvalidPictureLinkException(String clientMessage, String logMessage) {
		super(DestinationErrorCode.INVALID_PICTURE_LINK, logMessage);
		this.clientMessage = clientMessage;
	}
}
