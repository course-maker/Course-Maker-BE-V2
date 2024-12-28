package net.coursemaker.backendv2.auth.token;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.coursemaker.backendv2.auth.token.exception.ExpiredJwtTokenException;
import net.coursemaker.backendv2.auth.token.exception.InvalidTokenException;
import net.coursemaker.backendv2.auth.token.exception.ManipulatedTokenException;
import net.coursemaker.backendv2.auth.token.repository.RefreshTokenRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "TOKEN")
@Component
public class JwtProvider {

	private final SecretKey secretKey;
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.access.expiration}")
	private Long ACCESS_TOKEN_EXPIRATION;

	@Value("${jwt.refresh.expiration}")
	private Long REFRESH_TOKEN_EXPIRATION;

	public JwtProvider(
		@Value("${jwt.secret}") String secret,
		RefreshTokenRepository refreshTokenRepository) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts
				.SIG
				.HS256
				.key()
				.build()
				.getAlgorithm()
		);
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public String createAccessToken(Long userId, String nickname, String role) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
			.header()
				.keyId("access")
			.and()
			.subject(nickname)// 닉네임
			.claim("userId", userId)
			.claim("role", role)
			.issuer("CourseMaker")
			.issuedAt(new Date(now) )
			.expiration(new Date(now + ACCESS_TOKEN_EXPIRATION) )
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(Long userId, String nickname, String role) {
		long now = System.currentTimeMillis();
		String token = Jwts.builder()
			.header()
				.keyId("refresh")
			.and()
			.subject(nickname)// 닉네임
			.claim("userId", userId)
			.claim("role", role)
			.issuer("CourseMaker")
			.issuedAt(new Date(now))
			.expiration(new Date(now + REFRESH_TOKEN_EXPIRATION) )
			.signWith(secretKey)
			.compact();

		saveRefreshToken(token, new Date(now + REFRESH_TOKEN_EXPIRATION));

		return token;
	}

	public TokenType getTokenType(String token) {
		validateTokenIsManipulated(token);
		validateTokenIsExpired(token);

		/*헤더 추출*/
		JwsHeader header = Jwts
			.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getHeader();

		/*헤더에서 토큰 종류 추출*/
		String keyId = header.getKeyId();
		if (keyId.equals("access")) {
			return TokenType.ACCESS_TOKEN;
		} else if (keyId.equals("refresh")) {
			return TokenType.REFRESH_TOKEN;
		} else {
			throw new InvalidTokenException(
				"인증되지 않은 토큰 형태 입니다.",
				"인증되지 않은 토큰 형태: " + keyId);
		}
	}

	public Boolean isExpired(String token) {
		try {
			Jwts
				.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date(System.currentTimeMillis()));
		} catch (ExpiredJwtException e) {
			return true;
		}
		return false;
	}

	public String getNickname(String token) {
		validateTokenIsManipulated(token);
		validateTokenIsExpired(token);

		return Jwts
			.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}

	public String getRoles(String token) {
		validateTokenIsManipulated(token);
		validateTokenIsExpired(token);

		return (String)Jwts
			.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role");
	}

	public Long getUserId(String token) {
		validateTokenIsManipulated(token);
		validateTokenIsExpired(token);

		return Jwts
			.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("userId", Long.class);
	}

	public String reIssue(String refreshToken) {
		validateTokenIsManipulated(refreshToken);
		validateTokenIsExpired(refreshToken);
		validateRefreshTokenIsValid(refreshToken);

		return createAccessToken(
			getUserId(refreshToken),
			getNickname(refreshToken),
			getRoles(refreshToken));
	}

	public void expireRefreshToken(String refreshToken) {
		refreshTokenRepository.deleteByToken(refreshToken);
	}

	/*DB에 리프레시 토큰 저장*/
	private void saveRefreshToken(String refreshToken, Date expiredAt) {
		RefreshToken token = new RefreshToken(
			refreshToken,
			expiredAt
		);
		refreshTokenRepository.save(token);
	}

	private void validateTokenIsExpired(String token) {
		if (isExpired(token)) {
			throw new ExpiredJwtTokenException("Refresh 토큰이 만료됬습니다.", "[JWT] refresh token 만료: " + token);
		}
	}

	/*토큰 변조 여부*/
	private void validateTokenIsManipulated(String token) {
		try {
			/*페이로드 변조 여부 검증*/
			Jwts
				.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

			/*헤더 변조 여부 검증*/
			Jwts
				.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (UnsupportedJwtException e) {
			throw new ManipulatedTokenException(
				"토큰이 변조됬습니다. 해킹 위험이 있으니 즉시 로그아웃 하십시오.",
				"[JWT] 토큰 헤더 변조. 변조 내용: " + e.getMessage());
		} catch (SignatureException e) {
			throw new ManipulatedTokenException(
				"토큰이 변조됬습니다. 해킹 위험이 있으니 즉시 로그아웃 하십시오.",
				"[JWT] 토큰 클레임 변조. 변조 내용: " + e.getMessage());
		}
	}

	private void validateRefreshTokenIsValid(String token) {
		boolean exist = refreshTokenRepository.existsByToken(token);
		if (!exist) {
			throw new InvalidTokenException("인증되지 않은 토큰입니다.", "[JWT] 인증도지 않은 토큰: " + token);
		}
	}
}
