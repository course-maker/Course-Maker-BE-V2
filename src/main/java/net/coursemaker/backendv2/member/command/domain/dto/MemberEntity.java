package net.coursemaker.backendv2.member.command.domain.dto;

import java.time.LocalDateTime;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.aggregate.Role;
import net.coursemaker.backendv2.member.command.domain.aggregate.SignUpType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

	private Long id;
	private String email;
	private String name;
	private String nickname;
	private String phoneNumber;
	private Boolean isBlocked; // 정지된 사용자
	private Boolean marketingAgree; // 선택약관 동의여부
	private SignUpType signUpType;
	private String password;
	private Role role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	public static MemberEntity fromAggregate(Member member) {
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.id = member.getId();
		memberEntity.email = member.getEmail();
		memberEntity.name = member.getName();
		memberEntity.nickname = member.getNickname();
		memberEntity.phoneNumber = member.getPhoneNumber();
		memberEntity.isBlocked = member.getIsBlocked();
		memberEntity.marketingAgree = member.getMarketingAgree();
		memberEntity.signUpType = member.getSignUpType();
		memberEntity.password = member.getPassword();
		memberEntity.role = member.getRole();
		memberEntity.createdAt = member.getCreatedAt();
		memberEntity.updatedAt = member.getUpdatedAt();
		memberEntity.deletedAt = member.getDeletedAt();
		return memberEntity;
	}
}
