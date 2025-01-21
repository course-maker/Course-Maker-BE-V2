package net.coursemaker.backendv2.tag.command.domain.exception;

import lombok.Getter;
import net.coursemaker.backendv2.common.RootException;

@Getter
public class TagNotFoundException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public TagNotFoundException(String clientMessage, String logMessage) {
		super(TagErrorCode.TAG_NOT_FOUND, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
