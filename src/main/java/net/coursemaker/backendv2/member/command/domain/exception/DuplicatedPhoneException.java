package net.coursemaker.backendv2.member.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class DuplicatedPhoneException extends RootException {

	private final String clientMessage;
	private final String logMessage;

	public DuplicatedPhoneException(String clientMessage, String logMessage) {
		super(MemberErrorCode.PHONE_NUMBER_DUPLICATED, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}

}
