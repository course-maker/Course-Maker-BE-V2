package net.coursemaker.backendv2.destination.command.application.exception;

public class DestinationNotFoundException extends RuntimeException {
	public DestinationNotFoundException(String message) {
		super(message);
	}
}
