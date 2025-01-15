package net.coursemaker.backendv2.auth.command.domain.dto;

import net.coursemaker.backendv2.auth.command.domain.type.LoginState;

import lombok.Getter;

@Getter
public class LoginResult {
	private final LoginState state;

	private final String accessToken;
	private final String refreshToken;
	private final Long loginUserNo;
	private final String roleKor;

	public LoginResult(LoginState state, String accessToken, String refreshToken, Long loginUserNo, String roleKor) {
		this.state = state;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.loginUserNo = loginUserNo;
		this.roleKor = roleKor;
	}
}
