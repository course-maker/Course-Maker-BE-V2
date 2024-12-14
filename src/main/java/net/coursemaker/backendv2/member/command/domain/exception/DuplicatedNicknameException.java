package net.coursemaker.backendv2.member.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class DuplicatedNicknameException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public DuplicatedNicknameException(String clientMessage, String logMessage) {
		super(MemberErrorCode.NICKNAME_DUPLICATED, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
