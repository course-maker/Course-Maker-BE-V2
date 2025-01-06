package net.coursemaker.backendv2.member.command.domain.exception.email;

import net.coursemaker.backendv2.common.RootException;
import net.coursemaker.backendv2.member.command.domain.exception.MemberErrorCode;

import lombok.Getter;


@Getter
public class VerificationCodeNotSendException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public VerificationCodeNotSendException(String clientMessage, String logMessage) {
		super(MemberErrorCode.EMAIL_VERIFICATION_NOT_SEND, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
