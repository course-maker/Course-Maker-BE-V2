package net.coursemaker.backendv2.auth.command.application.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.auth.command.application.dto.LoginRequestApplicationDTO;
import net.coursemaker.backendv2.auth.command.application.dto.LoginResponseApplicationDTO;
import net.coursemaker.backendv2.auth.command.application.dto.LogoutRequestApplicationDTO;
import net.coursemaker.backendv2.auth.command.application.type.LoginType;
import net.coursemaker.backendv2.auth.command.domain.dto.EmailLoginRequestDTO;
import net.coursemaker.backendv2.auth.command.domain.dto.LoginResult;
import net.coursemaker.backendv2.auth.command.domain.dto.LogoutRequestDTO;
import net.coursemaker.backendv2.auth.command.domain.service.EmailLoginService;
import net.coursemaker.backendv2.auth.command.domain.service.EmailLogoutService;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthApplicationService {
	private final EmailLoginService emailLoginService;
	private final EmailLogoutService emailLogoutService;

	public LoginResponseApplicationDTO login(LoginRequestApplicationDTO login) {

		LoginResult result = null;

		/*이메일 로그인*/
		if (login.getType().equals(LoginType.EMAIL)) {
			EmailLoginRequestDTO dto = EmailLoginRequestDTO.builder()
				.email(login.getEmail())
				.password(login.getPassword())
				.build();
			result = emailLoginService.login(dto);
		}

		LoginResponseApplicationDTO response = LoginResponseApplicationDTO.builder()
			.accessToken(result.getAccessToken())
			.refreshToken(result.getRefreshToken())
			.roleKor(result.getRoleKor())
			.build();

		return response;
	}

	public void logout(LogoutRequestApplicationDTO logout) {
		LogoutRequestDTO dto = new LogoutRequestDTO(logout.getToken());
		emailLogoutService.logout(dto);
	}
}
