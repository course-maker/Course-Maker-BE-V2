package net.coursemaker.backendv2.auth.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseApplicationDTO {
	private String accessToken;
	private String refreshToken;
	private String roleKor;

	@Builder
	public LoginResponseApplicationDTO(String accessToken, String refreshToken, String roleKor) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.roleKor = roleKor;
	}
}
