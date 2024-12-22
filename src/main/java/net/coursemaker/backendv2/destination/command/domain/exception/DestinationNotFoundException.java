package net.coursemaker.backendv2.destination.command.domain.exception;

public class DestinationNotFoundException extends RuntimeException {
	public DestinationNotFoundException(String message) {
		super(message);
	}
}
