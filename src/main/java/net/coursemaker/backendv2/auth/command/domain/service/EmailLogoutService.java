package net.coursemaker.backendv2.auth.command.domain.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.auth.command.domain.dto.LogoutRequestDTO;
import net.coursemaker.backendv2.auth.token.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailLogoutService {

	JwtProvider jwtProvider;

	public void logout(LogoutRequestDTO request) {

		/*리프레시토큰 삭제*/
		jwtProvider.expireRefreshToken(request.getRefreshToken());

		Long userId = jwtProvider.getUserId(request.getRefreshToken());
		/*context holder 초기화*/
		SecurityContextHolder.clearContext();

		log.info("로그아웃 성공. 사용자 id: {}", userId);
	}
}
