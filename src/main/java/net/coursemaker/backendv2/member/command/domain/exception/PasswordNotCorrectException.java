package net.coursemaker.backendv2.member.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

public class PasswordNotCorrectException extends RootException {

	private final String clientMessage;
	private final String logMessage;

	public PasswordNotCorrectException(String clientMessage, String logMessage) {
		super(MemberErrorCode.MEMBER_NOT_FOUND, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
