package net.coursemaker.backendv2.member.command.domain.service;

import net.coursemaker.backendv2.common.exception.DataFormatMisMatchException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.dto.MemberBasicSignUpInfo;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedEmailException;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedNicknameException;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedPhoneException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class MemberSignUpServiceTest {

	@Mock
	private MemberCommandRepository commandRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private MemberSignUpService memberSignupService;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		memberSignupService = new MemberSignUpService(commandRepository, passwordEncoder);
	}

	@Test
	@DisplayName("정상 회원가입")
	void 정상_회원가입() {
		// Given
		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email@email.com");
		signupInfo.setPassword("password1!");
		signupInfo.setName("이름");
		signupInfo.setNickname("닉네임");
		signupInfo.setPhone("010-1234-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertDoesNotThrow(() -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("잘못된 이름 형식")
	void 이름_잘못됨() {
		// Given
		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email@email.com");
		signupInfo.setPassword("password1!");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("wrong");
		signupInfo.setPhone("010-1234-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DataFormatMisMatchException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("잘못된 이메일 형식")
	void 이메일_잘못됨() {
		// Given
		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("wrongEmail");
		signupInfo.setPassword("password1!");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("이름");
		signupInfo.setPhone("010-1234-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DataFormatMisMatchException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("잘못된 전화번호 형식")
	void 전화번호_잘못됨() {
		// Given
		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email@email.com");
		signupInfo.setPassword("password1!");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("이름");
		signupInfo.setPhone("010-12-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DataFormatMisMatchException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("잘못된 비밀번호 형식")
	void 비밀번호_잘못됨() {
		// Given
		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email@email.com");
		signupInfo.setPassword("password1");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("이름");
		signupInfo.setPhone("010-1234-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DataFormatMisMatchException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("이미 존재하는 닉네임")
	void 닉네임_중복() {
		// Given

		Member existMember = new Member(
			"email@email.com",
			"password1!",
			"이름",
			"닉네임",
			"010-1234-1234",
			true
		);
		when(commandRepository.findByNicknameAndDeletedAtIsNull(anyString())).thenReturn(Optional.of(existMember));

		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email1@email.com");
		signupInfo.setPassword("password1!");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("이름");
		signupInfo.setPhone("010-1234-5678");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DuplicatedNicknameException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("이미 존재하는 이메일")
	void 이메일_중복() {
		// Given

		Member existMember = new Member(
			"email@email.com",
			"password1!",
			"이름",
			"닉네임12",
			"010-1234-5555",
			true
		);
		when(commandRepository.findByEmailAndDeletedAtIsNull(anyString())).thenReturn(Optional.of(existMember));

		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email@email.com");
		signupInfo.setPassword("password1!");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("이름");
		signupInfo.setPhone("010-1234-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DuplicatedEmailException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("이미 존재하는 전화번호")
	void 전화_중복() {
		// Given

		Member existMember = new Member(
			"email12@email.com",
			"password1!",
			"이름",
			"닉네임12",
			"010-1234-1234",
			true
		);
		when(commandRepository.findByPhoneNumberAndDeletedAtIsNull(anyString())).thenReturn(Optional.of(existMember));

		MemberBasicSignUpInfo signupInfo = new MemberBasicSignUpInfo();
		signupInfo.setEmail("email@email.com");
		signupInfo.setPassword("password1!");
		signupInfo.setNickname("닉네임");
		signupInfo.setName("이름");
		signupInfo.setPhone("010-1234-1234");
		signupInfo.setMarketingAgree(true);

		// When
		assertThrows(DuplicatedPhoneException.class, () -> memberSignupService.basicSignUp(signupInfo));

		// Then
		verify(commandRepository, never()).save(any(Member.class));
	}
}
