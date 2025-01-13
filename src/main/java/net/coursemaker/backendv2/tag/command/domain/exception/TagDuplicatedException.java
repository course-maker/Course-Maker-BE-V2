package net.coursemaker.backendv2.tag.command.domain.exception;

import lombok.Getter;
import net.coursemaker.backendv2.common.RootException;

@Getter
public class TagDuplicatedException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public TagDuplicatedException(String clientMessage, String logMessage) {
		super(TagErrorCode.TAG_DUPLICATED, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
