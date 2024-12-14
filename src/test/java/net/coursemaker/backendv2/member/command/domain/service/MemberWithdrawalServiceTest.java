package net.coursemaker.backendv2.member.command.domain.service;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.dto.MemberWithdrawalInfo;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.exception.PasswordNotCorrectException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberWithdrawalServiceTest {

	@Mock
	private MemberCommandRepository commandRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberWithdrawalService memberWithdrawalService;


	@Nested
	@DisplayName("회원 탈퇴 서비스 테스트")
	class WithdrawalTests {

		private Member member;
		private MemberWithdrawalInfo withdrawalInfo;

		@BeforeEach
		void setUp() {
			member = mock(Member.class);
			withdrawalInfo = mock(MemberWithdrawalInfo.class);
		}

		@Test
		@DisplayName("일반 회원 탈퇴 성공")
		void withdrawal_success_normal_member() {
			// given
			when(withdrawalInfo.getMemberId()).thenReturn(1L);
			when(withdrawalInfo.getPassword()).thenReturn("correctPassword");
			when(withdrawalInfo.getIsAdminDelete()).thenReturn(false);

			when(commandRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(member));
			when(member.isEmailLogin()).thenReturn(true);
			when(passwordEncoder.matches("correctPassword", member.getPassword())).thenReturn(true);

			// when
			memberWithdrawalService.withdrawal(withdrawalInfo);

			// then
			verify(member).withdraw();
			verify(commandRepository).save(member);
		}

		@Test
		@DisplayName("관리자에 의한 회원 탈퇴 성공")
		void withdrawal_success_admin_delete() {
			// given
			when(withdrawalInfo.getMemberId()).thenReturn(1L);
			when(withdrawalInfo.getIsAdminDelete()).thenReturn(true);

			when(commandRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(member));

			// when
			memberWithdrawalService.withdrawal(withdrawalInfo);

			// then
			verify(member).withdraw();
			verify(commandRepository).save(member);
		}

		@Test
		@DisplayName("존재하지 않는 회원 탈퇴 시 예외 발생")
		void withdrawal_fail_member_not_found() {
			// given
			when(withdrawalInfo.getMemberId()).thenReturn(1L);

			when(commandRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty());

			// when & then
			assertThrows(MemberNotFoundException.class, () ->
				memberWithdrawalService.withdrawal(withdrawalInfo)
			);
		}

		@Test
		@DisplayName("비밀번호 불일치로 회원 탈퇴 실패")
		void withdrawal_fail_password_not_correct() {
			// given
			when(withdrawalInfo.getMemberId()).thenReturn(1L);
			when(withdrawalInfo.getPassword()).thenReturn("wrongPassword");
			when(withdrawalInfo.getIsAdminDelete()).thenReturn(false);

			when(commandRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(member));
			when(member.isEmailLogin()).thenReturn(true);
			when(passwordEncoder.matches("wrongPassword", member.getPassword())).thenReturn(false);

			// when & then
			assertThrows(PasswordNotCorrectException.class, () ->
				memberWithdrawalService.withdrawal(withdrawalInfo)
			);
		}

	}
}
