package net.coursemaker.backendv2.auth.command.application.dto;

import net.coursemaker.backendv2.auth.command.application.type.LoginType;

import lombok.Getter;

@Getter
public class LoginRequestApplicationDTO {

	private String email;
	private String password;
	private String token;

	private LoginType type;

	public LoginRequestApplicationDTO ofEmail(String email, String password) {
		this.email = email;
		this.password = password;
		this.type = LoginType.EMAIL;
		return this;
	}


}
