package net.coursemaker.backendv2.member.command.domain.aggregate;

import java.time.LocalDateTime;

import net.coursemaker.backendv2.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	/** 마케팅 동의 철회 */
	public void marketingWithdrawn() {
		this.marketingAgree = false;
	}

	/** 마케팅 동의 */
	public void marketingAgree() {
		this.marketingAgree = true;
	}

	/** 카카오로 회원가입 */
	public void kakaoSignUp() {
		this.signUpType = SignUpType.KAKAO;
	}

	/** 여행자 등급 조정 */
	public void updateRole(Role role) {
		this.role = role;
	}

	public boolean isMarketingAgree() {
		return marketingAgree;
	}

	public boolean isEmailLogin() {
		return signUpType == SignUpType.EMAIL;
	}

	public boolean isKakaoLogin() {
		return signUpType == SignUpType.KAKAO;
	}

	public void withdraw() {
		this.setDeletedAt(LocalDateTime.now());
	}

	public void changePassword(String password) {
		this.password = password;
	}

	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public Member(String email, String password, String nickname, String phoneNumber, boolean marketingAgree) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.marketingAgree = marketingAgree;
		this.isBlocked = false;
		this.signUpType = SignUpType.EMAIL;
		this.role = Role.ROLE_BEGINNER_TRAVELER;
	}
}
