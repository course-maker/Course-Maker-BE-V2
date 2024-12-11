package net.coursemaker.backendv2.common.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class DataFormatMisMatchException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public DataFormatMisMatchException(String clientMessage, String logMessage) {
		super(CommonErrorCode.DATA_FORMAT_MISMATCH, logMessage);

		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
