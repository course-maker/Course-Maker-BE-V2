package net.coursemaker.backendv2.auth.token.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RootException {

	private final String clientMessage;
	private final String logMessage;

	public InvalidTokenException(String clientMessage, String logMessage) {
		super(TokenErrorCode.INVALID_TOKEN, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
