package net.coursemaker.backendv2.member.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class DuplicatedEmailException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public DuplicatedEmailException(String clientMessage, String logMessage) {
		super(MemberErrorCode.EMAIL_DUPLICATED, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
