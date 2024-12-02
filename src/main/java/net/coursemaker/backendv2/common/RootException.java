package net.coursemaker.backendv2.common;

public class RootException extends RuntimeException {
	public RootException(ErrorCode errorCode, String logMessage) {
		super(logMessage);
	}
}
