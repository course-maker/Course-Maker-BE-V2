package net.coursemaker.backendv2.auth.command.dto;

import lombok.Data;

@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class EmailLoginRequestDTO {
	private String email;
	private String password;
}
