package net.coursemaker.backendv2.common;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	String getCode();

	HttpStatus getHttpStatus();

	String getReasonPhrase();
}
