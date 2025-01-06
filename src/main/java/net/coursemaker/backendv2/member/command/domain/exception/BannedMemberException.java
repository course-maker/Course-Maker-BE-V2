package net.coursemaker.backendv2.member.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;


@Getter
public class BannedMemberException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public BannedMemberException(String clientMessage, String logMessage) {
		super(MemberErrorCode.BANNED_MEMBER, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
