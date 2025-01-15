package net.coursemaker.backendv2.auth.command.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class EmailLoginRequestDTO {
	private String email;
	private String password;

	@Builder
	public EmailLoginRequestDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
