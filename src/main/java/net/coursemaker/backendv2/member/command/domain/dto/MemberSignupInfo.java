package net.coursemaker.backendv2.member.command.domain.dto;

import net.coursemaker.backendv2.member.command.domain.aggregate.SignUpType;

import lombok.Data;

@Data
public class MemberSignupInfo {
	private String nickname;
	private String password;
	private String email;
	private String phone;
	private boolean marketingAgree;
	private SignUpType signUpType;
}