package net.coursemaker.backendv2.member.command.domain.dto;

import lombok.Data;

@Data
public class MemberBasicSignUpInfo {
	private String nickname;
	private String name;
	private String password;
	private String email;
	private String phone;
	private boolean marketingAgree;
}
