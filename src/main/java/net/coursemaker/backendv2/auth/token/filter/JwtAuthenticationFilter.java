package net.coursemaker.backendv2.auth.token.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import net.coursemaker.backendv2.auth.command.dto.LoginedInfo;
import net.coursemaker.backendv2.auth.token.JwtProvider;
import net.coursemaker.backendv2.auth.token.TokenType;
import net.coursemaker.backendv2.auth.token.exception.ExpiredJwtTokenException;
import net.coursemaker.backendv2.auth.token.exception.InvalidTokenException;
import net.coursemaker.backendv2.member.command.domain.aggregate.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "TOKEN")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		/*토큰이 있는지 검증함*/
		if (authorization == null || !authorization.startsWith("Bearer ")) {

			/*익명 사용자 정보 주입*/
			LoginedInfo loginedInfo = new LoginedInfo(-1L, "익명 사용자", Role.ROLE_BEGINNER_TRAVELER);

			Authentication authToken = new UsernamePasswordAuthenticationToken(
				loginedInfo,
				null,
				null);
			SecurityContextHolder.getContext().setAuthentication(authToken);

			filterChain.doFilter(request, response);
			return;
		}

		log.info("[JWT] 토큰이 존재합니다. 토큰을 검증합니다.");

		String token = authorization.split(" ")[1];

		/*access token 여부 검증*/
		if (jwtProvider.getTokenType(token) != TokenType.ACCESS_TOKEN) {
			log.error("[JWT] 토큰인증 실패: {}", jwtProvider.getTokenType(token));
			throw new InvalidTokenException("인증되지 않은 토큰 형태 입니다.", "토큰 타입 인증 실패: " + jwtProvider.getTokenType(token));
		}
		/*토큰 만료 여부 확인*/
		if (jwtProvider.isExpired(token)) {
			log.info("[JWT] access 토큰 만료: {}", jwtProvider.isExpired(token));
			throw new ExpiredJwtTokenException("토큰이 만료됬습니다.", "Access Token 만료됨.");
		}

		Long userId = jwtProvider.getUserId(token);
		String nickname = jwtProvider.getNickname(token);
		Role roles = Role.valueOf(jwtProvider.getRoles(token));
		LoginedInfo loginedInfo = new LoginedInfo(userId, nickname, roles);
		loginedInfo.makeLoginUser();

		/*인증용 1회용 객체*/
		Authentication authToken = new UsernamePasswordAuthenticationToken(
			loginedInfo,
			token,
			extractRoles(loginedInfo));

		/*컨텍스트 홀더에 사용자 정보를 담음*/
		SecurityContextHolder.getContext().setAuthentication(authToken);

		log.info("[JWT] 토큰 검증 성공. 사용자: {}", nickname);

		filterChain.doFilter(request, response);
	}

	private Collection<GrantedAuthority> extractRoles(LoginedInfo info) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add((GrantedAuthority) () -> info.getRole().getRole());

		return authorities;
	}
}
