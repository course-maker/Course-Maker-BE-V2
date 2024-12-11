package net.coursemaker.backendv2.member.command.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.common.exception.DataFormatMisMatchException;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.aggregate.SignUpType;
import net.coursemaker.backendv2.member.command.domain.dto.MemberSignupInfo;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedEmailException;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedNicknameException;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedPhoneException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDomainService {

	private final MemberCommandRepository commandRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(MemberSignupInfo signupInfo) {
		validateEmail(signupInfo.getEmail());
		validatePhoneNumber(signupInfo.getPhone());
		validateNickname(signupInfo.getNickname());
		validatePassword(signupInfo.getPassword());

		String encryptedPassword = passwordEncoder.encode(signupInfo.getPassword());
		Member member = new Member(
			signupInfo.getEmail(),
			encryptedPassword,
			signupInfo.getNickname(),
			signupInfo.getPhone(),
			signupInfo.isMarketingAgree()
		);

		if (signupInfo.getSignUpType() == SignUpType.KAKAO) {
			member.kakaoSignUp();
		}

		commandRepository.save(member);
	}

	private void validateEmail(String email) {
		/*이메일 형식 검증*/
		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (email == null || !email.matches(emailRegex)) {
			throw new DataFormatMisMatchException("이메일 형식이 올바르지 않습니다.", "이메일 형식 오류: " + email);
		}

		/*이메일 중복 검증*/
		commandRepository.findByEmail(email).ifPresent(member -> {
			/*탈퇴한 회원이 아닌 경우*/
			if (member.getDeletedAt() == null) {
				throw new DuplicatedEmailException("이미 가입된 이메일 입니다.", "이메일 중복: " + email);
			}
		});
	}

	private void validatePhoneNumber(String phoneNumber) {
		/*전화번호 형식 검증*/
		String phoneRegex = "^010-\\d{4}-\\d{4}$";
		if (phoneNumber == null || !phoneNumber.matches(phoneRegex)) {
			throw new DataFormatMisMatchException("전화번호 형식이 올바르지 않습니다.", "전화번호 형식 오류: " + phoneNumber);
		}

		/*전화번호 중복 검증*/
		commandRepository.findByPhoneNumber(phoneNumber).ifPresent(member -> {
			/*탈퇴한 회원이 아닌 경우*/
			if (member.getDeletedAt() == null) {
				throw new DuplicatedPhoneException("이미 가입된 전화번호 입니다.", "전화번호 중복: " + phoneNumber);
			}
		});
	}

	private void validateNickname(String nickname) {
		/*닉네임 형식 검증*/
		String nicknameRegex = "^[가-힣]{2,10}$";
		if (nickname == null || !nickname.matches(nicknameRegex)) {
			throw new DataFormatMisMatchException("닉네임은 2~10글자의 한글이어야 합니다.", "닉네임 형식 오류: " + nickname);
		}

		/*닉네임 중복 검증*/
		commandRepository.findByNickname(nickname).ifPresent(member -> {
			/*탈퇴한 회원이 아닌 경우*/
			if (member.getDeletedAt() == null) {
				throw new DuplicatedNicknameException("이미 존재하는 닉네임 입니다.", "닉네임 중복: " + nickname);
			}
		});
	}


	private void validatePassword(String password) {
		/*비밀번호 형식 검증*/
		String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{8,15}$";
		if (password == null || !password.matches(passwordRegex)) {
			throw new DataFormatMisMatchException(
				"비밀번호는 8~15글자의 영어소문자, 숫자, 특수문자로 구성되야 합니다.",
				"비밀번호 형식 오류: " + password);
		}
	}
}
