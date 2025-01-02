package net.coursemaker.backendv2.auth.command.dto;

import net.coursemaker.backendv2.common.exception.LoginRequiredException;
import net.coursemaker.backendv2.member.command.domain.aggregate.Role;

public class LoginedInfo {
	private boolean isLogIn;
	private final Long memberId;
	private final String nickname;
	private final Role role;

	public LoginedInfo(final Long memberId, final String nickname, final Role role) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.role = role;
		this.isLogIn = false;
	}

	public boolean isAdmin() {
		return role == Role.ROLE_ADMIN;
	}

	public boolean isLogin() {
		return this.isLogIn;
	}

	public void makeLoginUser() {
		this.isLogIn = true;
	}

	public Long getMemberId() {
		validateLoginUser();
		return memberId;
	}

	public String getNickname() {
		validateLoginUser();
		return nickname;
	}

	public Role getRole() {
		validateLoginUser();
		return role;
	}

	private void validateLoginUser() {
		if (!isLogIn) {
			throw new LoginRequiredException("로그인 후 사용이 가능합니다.", "비 로그인 사용자 접근");
		}
	}
}
