package net.coursemaker.backendv2.course.command.domain.exception;

import net.coursemaker.backendv2.common.RootException;

import lombok.Getter;

@Getter
public class CourseNotFoundException extends RootException {
	private final String clientMessage;
	private final String logMessage;

	public CourseNotFoundException(String clientMessage, String logMessage) {
		super(CourseErrorCode.ILLEGAL_COURSE_ARGUMENT, logMessage);
		this.clientMessage = clientMessage;
		this.logMessage = logMessage;
	}
}
