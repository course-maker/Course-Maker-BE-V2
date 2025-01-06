package net.coursemaker.backendv2.auth.command.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.auth.command.dto.EmailLoginRequestDTO;
import net.coursemaker.backendv2.auth.command.dto.LoginResult;
import net.coursemaker.backendv2.auth.command.dto.LoginState;
import net.coursemaker.backendv2.auth.token.JwtProvider;
import net.coursemaker.backendv2.member.command.domain.aggregate.Role;
import net.coursemaker.backendv2.member.command.domain.dto.MemberEntity;
import net.coursemaker.backendv2.member.command.domain.exception.BannedMemberException;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.exception.PasswordNotCorrectException;
import net.coursemaker.backendv2.member.command.domain.service.MemberUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j(topic = "AUTH")
@Service
@RequiredArgsConstructor
public class EmailLoginService {

	private final MemberUtils memberUtils;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public LoginResult login(EmailLoginRequestDTO request) {
		/*사용자 정보 객체 가져옴*/
		MemberEntity memberByEmail = memberUtils
			.getMemberByEmail(
				request.getEmail(),
				false)
			.get(0);

		validateMemberIsBanned(memberByEmail, request);

		/*로그인*/
		boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), memberByEmail.getPassword());

		/*비밀번호 틀림*/
		if (!isPasswordMatch) {
			throw new PasswordNotCorrectException(
				"비밀번호가 틀렸습니다",
				"로그인 실패. 사용자: "
					+ memberByEmail.getEmail());
		}

		/*로그인 성공 후 토큰 발급*/

		String accessToken = jwtProvider.createAccessToken(
			memberByEmail.getId(),
			memberByEmail.getNickname(),
			memberByEmail.getRole()
				.getRole());

		String refreshToken = jwtProvider.createRefreshToken(
			memberByEmail.getId(),
			memberByEmail.getNickname(),
			memberByEmail.getRole()
				.getRole());

		LoginResult result = new LoginResult(
			LoginState.SUCCESS,
			accessToken,
			refreshToken,
			memberByEmail.getId(),
			Role.toKor(memberByEmail.getRole().getRole())
		);

		log.info("로그인 성공. 사용자: {}", memberByEmail.getId());

		return result;
	}

	private void validateEmailIsExist(MemberEntity entity, EmailLoginRequestDTO request) {
		if (entity == null) {
			throw new MemberNotFoundException(
				"가입되지 않은 이메일 입니다",
				"로그인 실패. 가입되지 않은 이메일. email: "
					+ request.getEmail());
		}
	}

	private void validateMemberIsBanned(MemberEntity entity, EmailLoginRequestDTO request) {
		if (entity.getIsBlocked()) {
			throw new BannedMemberException(
				"정지된 사용자 입니다",
				"정지된 사용자 로그인. id: "
					+ request.getEmail());
		}
	}
}
