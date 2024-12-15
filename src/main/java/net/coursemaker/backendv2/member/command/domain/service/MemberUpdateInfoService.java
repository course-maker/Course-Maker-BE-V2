package net.coursemaker.backendv2.member.command.domain.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.common.exception.DataFormatMisMatchException;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.dto.MemberBasicUpdateInfo;
import net.coursemaker.backendv2.member.command.domain.exception.DuplicatedPhoneException;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.exception.PasswordNotCorrectException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class MemberUpdateInfoService {

	private final MemberCommandRepository commandRepository;
	private final PasswordEncoder passwordEncoder;

	public void changePhoneNumber(MemberBasicUpdateInfo info, String updatedPhoneNumber) {
		validateMemberIsExist(info);
		validatePasswordIsCorrect(info);
		validatePhoneNumberForm(updatedPhoneNumber);
		validatePhoneNumberIsExist(updatedPhoneNumber);

		Member updatedMember = getMemberAggregate(info);
		updatedMember.updatePhoneNumber(updatedPhoneNumber);

		commandRepository.save(updatedMember);
	}

	public void changeName(MemberBasicUpdateInfo info, String newName) {
		validateMemberIsExist(info);
		validatePasswordIsCorrect(info);

		Member updatedMember = getMemberAggregate(info);
		updatedMember.updateName(newName);

		commandRepository.save(updatedMember);
	}

	public void changePassword(MemberBasicUpdateInfo info, String newPassword) {
		validateMemberIsExist(info);
		validatePasswordIsCorrect(info);
		validatePasswordForm(newPassword);

		Member updatedMember = getMemberAggregate(info);

		String encodedPassword = passwordEncoder.encode(newPassword);
		updatedMember.changePassword(encodedPassword);
		commandRepository.save(updatedMember);
	}

	private Member getMemberAggregate(MemberBasicUpdateInfo info) {
		return commandRepository.findByIdAndDeletedAtIsNull(info.getMemberId())
			.orElseThrow(() ->
				new MemberNotFoundException(
					"수정할 사용자를 찾을 수 없습니다.",
					"사용자를 수정할 수 없음. id: " + info.getMemberId()
				)
			);
	}

	private void validateMemberIsExist(MemberBasicUpdateInfo info) {
		/*사용자 존재 여부 검증*/
		Member member = commandRepository.findByIdAndDeletedAtIsNull(info.getMemberId())
			.orElseThrow(() ->
				new MemberNotFoundException(
					"수정할 사용자를 찾을 수 없습니다.",
					"사용자를 수정할 수 없음. id: " + info.getMemberId()
				)
			);
	}

	private void validatePasswordIsCorrect(MemberBasicUpdateInfo info) {
		/*사용자 존재 여부 검증*/
		Member member = commandRepository.findByIdAndDeletedAtIsNull(info.getMemberId())
			.orElseThrow(() ->
				new MemberNotFoundException(
					"수정할 사용자를 찾을 수 없습니다.",
					"사용자를 수정할 수 없음. id: " + info.getMemberId()
				)
			);

		/*관리자는 다른 사용자 임의로 변경 가능*/
		if (info.getIsAdminUpdate()) {
			return;
		}

		/*이메일로 회원가입한 경우 비밀번호 일치여부 검증*/
		if (member.isEmailLogin()) {
			if (!passwordEncoder.matches(info.getPassword(), member.getPassword())) {
				throw new PasswordNotCorrectException(
					"비밀번호가 일치하지 않습니다.",
					"수정 실패: id: " + info.getMemberId()
				);
			}
		}
	}

	private void validatePhoneNumberForm(String phoneNumber) {
		/*전화번호 형식 검증*/
		String phoneRegex = "^010-\\d{4}-\\d{4}$";
		if (phoneNumber == null || !phoneNumber.matches(phoneRegex)) {
			throw new DataFormatMisMatchException(
				"전화번호 형식이 올바르지 않습니다.",
				"전화번호 형식 오류: " + phoneNumber);
		}
	}

	private void validatePhoneNumberIsExist(String phoneNumber) {
		/*전화번호 중복 검증*/
		commandRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNumber).ifPresent(member -> {
			/*탈퇴한 회원이 아닌 경우*/
			throw new DuplicatedPhoneException(
				"이미 가입된 전화번호 입니다.",
				"전화번호 중복: " + phoneNumber);
		});
	}

	private void validatePasswordForm(String password) {
		/*비밀번호 형식 검증*/
		String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{8,15}$";
		if (password == null || !password.matches(passwordRegex)) {
			throw new DataFormatMisMatchException(
				"비밀번호는 8~15글자의 영어 소문자, 숫자, 특수문자로 구성되야 합니다.",
				"비밀번호 형식 오류: " + password);
		}
	}
}
