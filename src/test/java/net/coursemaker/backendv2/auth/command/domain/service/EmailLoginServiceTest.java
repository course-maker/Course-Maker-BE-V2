package net.coursemaker.backendv2.auth.command.domain.service;

import net.coursemaker.backendv2.auth.command.domain.dto.EmailLoginRequestDTO;
import net.coursemaker.backendv2.auth.command.domain.dto.LoginResult;
import net.coursemaker.backendv2.auth.command.domain.type.LoginState;
import net.coursemaker.backendv2.auth.token.JwtProvider;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.aggregate.Role;
import net.coursemaker.backendv2.member.command.domain.exception.BannedMemberException;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.exception.PasswordNotCorrectException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;
import net.coursemaker.backendv2.member.command.domain.service.MemberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailLoginServiceTest {

	@Mock
	private MemberCommandRepository memberCommandRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@InjectMocks
	private MemberUtils memberUtils;

	@InjectMocks
	private EmailLoginService emailLoginService;

	@BeforeEach
	void setUp() {
		emailLoginService = new EmailLoginService(memberUtils, passwordEncoder, jwtProvider);
	}

	@Test
	void login_Success() {
		// Given
		String email = "test@test.com";
		String password = "password";
		String encodedPassword = "encodedPassword";
		Member member = new Member(
			email,
			encodedPassword,
			"이름",
			"닉네임",
			"010-1234-1234",
			true
			);

		member.updateRole(Role.ROLE_BEGINNER_TRAVELER);

		EmailLoginRequestDTO request = EmailLoginRequestDTO.builder()
			.email(email)
			.password(password)
			.build();

		when(memberCommandRepository.findAllByEmail(email))
			.thenReturn(List.of(member));
		when(passwordEncoder.matches(password, encodedPassword))
			.thenReturn(true);
		when(jwtProvider.createAccessToken(any(), anyString(), anyString()))
			.thenReturn("accessToken");
		when(jwtProvider.createRefreshToken(any(), anyString(), anyString()))
			.thenReturn("refreshToken");

		// When
		LoginResult result = emailLoginService.login(request);

		// Then
		assertThat(result.getState()).isEqualTo(LoginState.SUCCESS);
		assertThat(result.getAccessToken()).isEqualTo("accessToken");
		assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
		verify(memberCommandRepository).findAllByEmail(email);
		verify(passwordEncoder).matches(password, encodedPassword);
	}

	@Test
	void login_WhenPasswordIncorrect_ThrowsException() {
		// Given
		String email = "test@test.com";
		String password = "wrongPassword";
		Member member = new Member(
			email,
			password,
			"이름",
			"닉네임",
			"010-1234-1234",
			true
		);

		EmailLoginRequestDTO request = EmailLoginRequestDTO.builder()
			.email(email)
			.password(password)
			.build();

		when(memberCommandRepository.findAllByEmail(email))
			.thenReturn(List.of(member));
		when(passwordEncoder.matches(password, member.getPassword()))
			.thenReturn(false);

		// When & Then
		assertThrows(PasswordNotCorrectException.class,
			() -> emailLoginService.login(request));
	}

	@Test
	void login_WhenMemberBanned_ThrowsException() {
		// Given
		String email = "test@test.com";
		Member member = new Member(
			email,
			"password",
			"이름",
			"닉네임",
			"010-1234-1234",
			true
		);

		member.makeBlock();

		EmailLoginRequestDTO request = EmailLoginRequestDTO.builder()
			.email(email)
			.password("password")
			.build();

		when(memberCommandRepository.findAllByEmail(email))
			.thenReturn(List.of(member));

		// When & Then
		assertThrows(BannedMemberException.class,
			() -> emailLoginService.login(request));
	}

	@Test
	void login_WhenMemberNotFound_ThrowsException() {
		// Given
		String email = "nonexistent@test.com";
		EmailLoginRequestDTO request = EmailLoginRequestDTO.builder()
			.email(email)
			.password("password")
			.build();

		when(memberCommandRepository.findAllByEmail(email))
			.thenReturn(Collections.emptyList());

		// When & Then
		assertThrows(MemberNotFoundException.class,
			() -> emailLoginService.login(request));
	}
}
