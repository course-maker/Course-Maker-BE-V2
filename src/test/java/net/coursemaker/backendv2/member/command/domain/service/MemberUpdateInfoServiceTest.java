package net.coursemaker.backendv2.member.command.domain.service;

import net.coursemaker.backendv2.common.exception.DataFormatMisMatchException;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.dto.MemberBasicUpdateInfo;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedPhoneException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberUpdateInfoServiceTest {

	@Mock
	private MemberCommandRepository commandRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberUpdateInfoService memberUpdateInfoService;

	private Member mockMember;
	private MemberBasicUpdateInfo mockUpdateInfo;

	@BeforeEach
	void setUp() {
		mockMember = mock(Member.class);
		mockUpdateInfo = mock(MemberBasicUpdateInfo.class);
	}

	@Nested
	@DisplayName("휴대폰 번호 변경")
	class UpdatePhoneNumber{

		@Test
		@DisplayName("변경 성공")
		void success() {
			// given
			when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
			when(mockUpdateInfo.getMemberId()).thenReturn(1L);
			when(mockUpdateInfo.getPassword()).thenReturn("password1!");
			when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
			when(mockMember.isEmailLogin()).thenReturn(true);
			when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);
			when(commandRepository.findByPhoneNumberAndDeletedAtIsNull(anyString())).thenReturn(Optional.empty());

			// when
			assertDoesNotThrow(() ->
				memberUpdateInfoService.changePhoneNumber(mockUpdateInfo, "010-1234-5678")
			);

			// then
			verify(mockMember).updatePhoneNumber("010-1234-5678");
			verify(commandRepository).save(mockMember);
		}

		@Nested
		@DisplayName("실패 케이스")
		class FailCase{

			@Test
			@DisplayName("존재하지 않는 사용자일때")
			void invalidMember() {
				// Arrange
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);

				// Act & Assert
				assertThrows(MemberNotFoundException.class, () ->
					memberUpdateInfoService.changePhoneNumber(mockUpdateInfo, "010-1234-5678")
				);
			}

			@Test
			@DisplayName("비번 틀렸을때")
			void wrongPassword() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("password1!");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(false);

				// when, then
				assertThrows(PasswordNotCorrectException.class, () ->
					memberUpdateInfoService.changePhoneNumber(mockUpdateInfo, "010-1234-5678")
				);
			}

			@Test
			@DisplayName("휴대폰 번호 형태가 다를때")
			void wrongPhoneNumberFormat() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("password1!");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);

				// when, then
				assertThrows(DataFormatMisMatchException.class, () ->
					memberUpdateInfoService.changePhoneNumber(mockUpdateInfo, "wrongForm")
				);
			}

			@Test
			@DisplayName("이미 가입된 휴대폰 번호일때")
			void duplicatePhoneNumber() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("password1!");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);
				when(commandRepository.findByPhoneNumberAndDeletedAtIsNull(anyString())).thenReturn(Optional.of(mockMember));

				// when, then
				assertThrows(DuplicatedPhoneException.class, () ->
					memberUpdateInfoService.changePhoneNumber(mockUpdateInfo, "010-1234-5678")
				);
			}
		}
	}

	@Nested
	@DisplayName("이름 변경")
	class UpdateName{

		@Test
		@DisplayName("변경 성공")
		void success() {
			// given
			when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
			when(mockUpdateInfo.getMemberId()).thenReturn(1L);
			when(mockUpdateInfo.getPassword()).thenReturn("password1!");
			when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
			when(mockMember.isEmailLogin()).thenReturn(true);
			when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);

			// when
			assertDoesNotThrow(() ->
				memberUpdateInfoService.changeName(mockUpdateInfo, "NewName")
			);

			// then
			verify(mockMember).updateName("NewName");
			verify(commandRepository).save(mockMember);
		}

		@Nested
		@DisplayName("실패 케이스")
		class FailCase{
			@Test
			@DisplayName("비번 틀렸을때")
			void wrongPassword() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("password1!");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(false);

				// when, then
				assertThrows(PasswordNotCorrectException.class, () ->
					memberUpdateInfoService.changeName(mockUpdateInfo, "010-1234-5678")
				);
			}

			@Test
			@DisplayName("존재하지 않는 사용자일때")
			void invalidMember() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);

				// when, then
				assertThrows(MemberNotFoundException.class, () ->
					memberUpdateInfoService.changeName(mockUpdateInfo, "newName")
				);
			}
		}
	}

	@Nested
	@DisplayName("비밀번호 변경")
	class ChangePassword{

		@Test
		@DisplayName("변경 성공")
		void success() {
			// given
			when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
			when(mockUpdateInfo.getMemberId()).thenReturn(1L);
			when(mockUpdateInfo.getPassword()).thenReturn("oldPassword");
			when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
			when(mockMember.isEmailLogin()).thenReturn(true);
			when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
			when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);

			// when
			assertDoesNotThrow(() ->
				memberUpdateInfoService.changePassword(mockUpdateInfo, "password1!")
			);

			// then
			verify(mockMember).changePassword("encodedPassword");
			verify(commandRepository).save(mockMember);
		}

		@Nested
		@DisplayName("실패 케이스")
		class FailCase{
			@Test
			@DisplayName("비번 형식이 틀렸을때")
			void wrongFormat() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("oldPassword");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);

				// when, then
				assertThrows(DataFormatMisMatchException.class, () ->
					memberUpdateInfoService.changePassword(mockUpdateInfo, "wrongFormat")
				);
			}

			@Test
			@DisplayName("비밀번호가 틀렸을때")
			void wrongPassword() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("oldPassword");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(false);

				// when, then
				assertThrows(PasswordNotCorrectException.class, () ->
					memberUpdateInfoService.changePassword(mockUpdateInfo, "wrongPassword")
				);
			}
		}
	}


	@Nested
	@DisplayName("마케팅 동의")
	class MarketingAgree {

		@Nested
		@DisplayName("동의")
		class Agree {

			@Test
			@DisplayName("동의 성공")
			void success() {
				// given
				when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
				when(mockUpdateInfo.getMemberId()).thenReturn(1L);
				when(mockUpdateInfo.getPassword()).thenReturn("oldPassword");
				when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
				when(mockMember.isEmailLogin()).thenReturn(true);
				when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);

				// when
				assertDoesNotThrow(() ->
					memberUpdateInfoService.agreeMarketingAgreement(mockUpdateInfo)
				);

				// then
				verify(mockMember).marketingAgree();
				verify(commandRepository).save(mockMember);
			}

			@Nested
			@DisplayName("실패 케이스")
			class FailCase {
				@Test
				@DisplayName("존재하지 않는 사용자일때")
				void invalidMember() {
					// given
					when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());
					when(mockUpdateInfo.getMemberId()).thenReturn(1L);

					// when, then
					assertThrows(MemberNotFoundException.class, () ->
						memberUpdateInfoService.agreeMarketingAgreement(mockUpdateInfo)
					);
				}
			}
		}

		@Nested
		@DisplayName("마케팅 동의 철회")
		class WithdrawMarketingAgree {

			@Nested
			@DisplayName("동의 철회")
			class Agree {

				@Test
				@DisplayName("동의 철회 성공")
				void success() {
					// given
					when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(mockMember));
					when(mockUpdateInfo.getMemberId()).thenReturn(1L);
					when(mockUpdateInfo.getPassword()).thenReturn("oldPassword");
					when(mockUpdateInfo.getIsAdminUpdate()).thenReturn(false);
					when(mockMember.isEmailLogin()).thenReturn(true);
					when(passwordEncoder.matches(mockUpdateInfo.getPassword(), mockMember.getPassword())).thenReturn(true);

					// when
					assertDoesNotThrow(() ->
						memberUpdateInfoService.withdrawMarketingAgreement(mockUpdateInfo)
					);

					// then
					verify(mockMember).marketingWithdrawn();
					verify(commandRepository).save(mockMember);
				}

				@Nested
				@DisplayName("실패 케이스")
				class FailCase {
					@Test
					@DisplayName("존재하지 않는 사용자일때")
					void invalidMember() {
						// given
						when(commandRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());
						when(mockUpdateInfo.getMemberId()).thenReturn(1L);

						// when, then
						assertThrows(MemberNotFoundException.class, () ->
							memberUpdateInfoService.withdrawMarketingAgreement(mockUpdateInfo)
						);
					}
				}
			}
		}
	}
}
