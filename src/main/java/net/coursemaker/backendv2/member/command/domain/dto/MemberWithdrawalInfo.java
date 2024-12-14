package net.coursemaker.backendv2.member.command.domain.dto;

import lombok.Data;

@Data
public class MemberWithdrawalInfo {
	Long memberId;
	private Boolean isAdminDelete;
	private String password;
}
