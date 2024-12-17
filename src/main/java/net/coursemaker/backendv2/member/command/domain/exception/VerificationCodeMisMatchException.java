package net.coursemaker.backendv2.member.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class VerificationCodeMisMatchException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public VerificationCodeMisMatchException(String clientMessage, String logMessage) {
		super(MemberErrorCode.EMAIL_VERIFICATION_MISMATCH, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
