package net.coursemaker.backendv2.auth.token;

import net.coursemaker.backendv2.auth.token.exception.InvalidTokenException;
import net.coursemaker.backendv2.auth.token.exception.ManipulatedTokenException;
import net.coursemaker.backendv2.auth.token.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	private JwtProvider jwtProvider;

	private final String SECRET_KEY = "yourTestSecretKeyHereWithAtLeast32Characters";
	private final Long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1시간
	private final Long REFRESH_TOKEN_EXPIRATION = 1209600000L; // 2주

	private final Long TEST_USER_ID = 1L;
	private final String TEST_NICKNAME = "testUser";
	private final String TEST_ROLE = "ROLE_USER";

	@BeforeEach
	void setUp() {
		jwtProvider = new JwtProvider(SECRET_KEY, refreshTokenRepository);
		ReflectionTestUtils.setField(jwtProvider, "ACCESS_TOKEN_EXPIRATION", ACCESS_TOKEN_EXPIRATION);
		ReflectionTestUtils.setField(jwtProvider, "REFRESH_TOKEN_EXPIRATION", REFRESH_TOKEN_EXPIRATION);
	}

	@Test
	@DisplayName("액세스 토큰 생성 테스트")
	void createAccessToken_ShouldGenerateValidToken() {
		// given
		// TEST_USER_ID, TEST_NICKNAME, TEST_ROLE은 이미 설정되어 있음

		// when
		String accessToken = jwtProvider.createAccessToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);

		// then
		assertAll(
			() -> assertNotNull(accessToken),
			() -> assertEquals(TEST_NICKNAME, jwtProvider.getNickname(accessToken)),
			() -> assertEquals(TEST_USER_ID, jwtProvider.getUserId(accessToken)),
			() -> assertEquals(TEST_ROLE, jwtProvider.getRoles(accessToken)),
			() -> assertEquals(TokenType.ACCESS_TOKEN, jwtProvider.getTokenType(accessToken)),
			() -> assertFalse(jwtProvider.isExpired(accessToken))
		);
	}

	@Test
	@DisplayName("리프레시 토큰 생성 테스트")
	void createRefreshToken_ShouldGenerateValidToken() {
		// given
		when(refreshTokenRepository.save(any(RefreshToken.class)))
			.thenReturn(new RefreshToken("token", new Date()));

		// when
		String refreshToken = jwtProvider.createRefreshToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);

		// then
		assertAll(
			() -> assertNotNull(refreshToken),
			() -> assertEquals(TEST_NICKNAME, jwtProvider.getNickname(refreshToken)),
			() -> assertEquals(TEST_USER_ID, jwtProvider.getUserId(refreshToken)),
			() -> assertEquals(TEST_ROLE, jwtProvider.getRoles(refreshToken)),
			() -> assertEquals(TokenType.REFRESH_TOKEN, jwtProvider.getTokenType(refreshToken)),
			() -> assertFalse(jwtProvider.isExpired(refreshToken)),
			() -> verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class))
		);
	}

	@Test
	@DisplayName("만료된 토큰 검증 테스트")
	void isExpired_ShouldReturnTrue_WhenTokenIsExpired() {
		// given
		ReflectionTestUtils.setField(jwtProvider, "ACCESS_TOKEN_EXPIRATION", -1000L); // 즉시 만료되도록 설정
		String expiredToken = jwtProvider.createAccessToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);

		// when
		boolean isExpired = jwtProvider.isExpired(expiredToken);

		// then
		assertTrue(isExpired);
	}

	@Test
	@DisplayName("토큰 재발행 테스트")
	void reIssue_ShouldCreateNewAccessToken() {
		// given
		String refreshToken = jwtProvider.createRefreshToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);
		when(refreshTokenRepository.existsByToken(refreshToken)).thenReturn(true);

		// when
		String newAccessToken = jwtProvider.reIssue(refreshToken);

		// then
		assertAll(
			() -> assertNotNull(newAccessToken),
			() -> assertEquals(TEST_NICKNAME, jwtProvider.getNickname(newAccessToken)),
			() -> assertEquals(TEST_USER_ID, jwtProvider.getUserId(newAccessToken)),
			() -> assertEquals(TEST_ROLE, jwtProvider.getRoles(newAccessToken)),
			() -> assertEquals(TokenType.ACCESS_TOKEN, jwtProvider.getTokenType(newAccessToken)),
			() -> verify(refreshTokenRepository, times(1)).existsByToken(refreshToken)
		);
	}

	@Test
	@DisplayName("유효하지 않은 리프레시 토큰으로 재발행 시도 시 예외 발생")
	void reIssue_ShouldThrowException_WhenRefreshTokenInvalid() {
		// given
		String refreshToken = jwtProvider.createRefreshToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);
		when(refreshTokenRepository.existsByToken(refreshToken)).thenReturn(false);

		// when & then
		assertThrows(InvalidTokenException.class, () -> jwtProvider.reIssue(refreshToken));
	}

	@Test
	@DisplayName("리프레시 토큰 만료 테스트")
	void expireRefreshToken_ShouldDeleteToken() {
		// given
		String refreshToken = "testRefreshToken";

		// when
		jwtProvider.expireRefreshToken(refreshToken);

		// then
		verify(refreshTokenRepository, times(1)).deleteByToken(refreshToken);
	}



	@Test
	@DisplayName("변조된 토큰 검증 시 예외 발생 테스트")
	void validateToken_ShouldThrowException_WhenTokenIsTampered() {
		// given
		String originalToken = jwtProvider.createAccessToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);
		String tamperedToken = originalToken.substring(0, originalToken.length() - 5) + "tamper";

		// when & then
		assertThrows(ManipulatedTokenException.class, () -> jwtProvider.getNickname(tamperedToken));
	}

	@Test
	@DisplayName("다른 시크릿 키로 서명된 토큰 검증 시 예외 발생 테스트")
	void validateToken_ShouldThrowException_WhenSignedWithDifferentKey() {
		// given
		// 다른 시크릿 키로 JwtProvider 인스턴스 생성
		JwtProvider anotherProvider = new JwtProvider("anotherSecretKeyHereWithAtLeast32Chars", refreshTokenRepository);
		ReflectionTestUtils.setField(anotherProvider, "ACCESS_TOKEN_EXPIRATION", ACCESS_TOKEN_EXPIRATION);

		// 다른 시크릿 키로 토큰 생성
		String tokenFromDifferentKey = anotherProvider.createAccessToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);

		// when & then
		assertThrows(ManipulatedTokenException.class, () -> jwtProvider.getNickname(tokenFromDifferentKey));
	}

	@Test
	@DisplayName("페이로드가 변조된 토큰 검증 시 예외 발생 테스트")
	void validateToken_ShouldThrowException_WhenPayloadIsTampered() {
		// given
		String originalToken = jwtProvider.createAccessToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);

		// JWT는 header.payload.signature 형식이므로 payload 부분만 변조
		String[] parts = originalToken.split("\\.");
		String tamperedPayload = Base64.getEncoder()
			.encodeToString("{\"sub\":\"hacker\"}".getBytes());
		String tamperedToken = parts[0] + "." + tamperedPayload + "." + parts[2];

		// when & then
		assertThrows(ManipulatedTokenException.class, () -> jwtProvider.getNickname(tamperedToken));
	}

	@Test
	@DisplayName("헤더가 변조된 토큰 검증 시 예외 발생 테스트")
	void validateToken_ShouldThrowException_WhenHeaderIsTampered() {
		// given
		String originalToken = jwtProvider.createAccessToken(TEST_USER_ID, TEST_NICKNAME, TEST_ROLE);

		// JWT 헤더 부분을 변조
		String[] parts = originalToken.split("\\.");
		String tamperedHeader = Base64.getEncoder()
			.encodeToString("{\"alg\":\"none\"}".getBytes());
		String tamperedToken = tamperedHeader + "." + parts[1] + "." + parts[2];

		// when & then
		assertThrows(ManipulatedTokenException.class, () -> jwtProvider.getNickname(tamperedToken));
	}

	@Test
	@DisplayName("빈 토큰 검증 시 예외 발생 테스트")
	void validateToken_ShouldThrowException_WhenTokenIsEmpty() {
		// given
		String emptyToken = "";

		// when & then
		assertThrows(IllegalArgumentException.class, () -> jwtProvider.getNickname(emptyToken));
	}
}
