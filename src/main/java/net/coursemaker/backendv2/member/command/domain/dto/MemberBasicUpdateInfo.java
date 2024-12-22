package net.coursemaker.backendv2.member.command.domain.dto;

import lombok.Data;

@Data
public class MemberBasicUpdateInfo {
	Long memberId;
	private Boolean isAdminUpdate;
	private String password;
}
