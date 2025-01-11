package net.coursemaker.backendv2.auth.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;
import net.coursemaker.backendv2.common.exception.type.CommonErrorCode;

import lombok.Getter;

@Getter
public class LoginRequiredException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public LoginRequiredException(String clientMessage, String logMessage) {
		super(CommonErrorCode.LOGIN_REQUIRED, logMessage);

		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
