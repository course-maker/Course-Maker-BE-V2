package net.coursemaker.backendv2.auth.command.domain.dto;

import lombok.Getter;

@Getter
public class LogoutRequestDTO {

	private String refreshToken;

	public LogoutRequestDTO(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
