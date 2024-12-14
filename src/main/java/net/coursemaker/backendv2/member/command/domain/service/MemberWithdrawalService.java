package net.coursemaker.backendv2.member.command.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.dto.MemberWithdrawalInfo;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.exception.PasswordNotCorrectException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWithdrawalService {
	private final MemberCommandRepository commandRepository;
	private final PasswordEncoder passwordEncoder;

	public void withdrawal(MemberWithdrawalInfo withdrawalInfo) {
		validateMemberIsExist(withdrawalInfo);
		validatePasswordIsCorrect(withdrawalInfo);

		Member deletedMember = commandRepository.findByIdAndDeletedAtIsNull(withdrawalInfo.getMemberId()).get();

		deletedMember.withdraw();

		commandRepository.save(deletedMember);
	}

	private void validateMemberIsExist(MemberWithdrawalInfo withdrawalInfo) {
		/*사용자 존재 여부 검증*/
		Member member = commandRepository.findByIdAndDeletedAtIsNull(withdrawalInfo.getMemberId())
			.orElseThrow(() ->
				new MemberNotFoundException(
					"삭제할 사용자를 찾을 수 없습니다.",
					"사용자를 삭제할 수 없음. id: " + withdrawalInfo.getMemberId()
				)
			);
	}

	private void validatePasswordIsCorrect(MemberWithdrawalInfo withdrawalInfo) {
		/*사용자 존재 여부 검증*/
		Member member = commandRepository.findByIdAndDeletedAtIsNull(withdrawalInfo.getMemberId())
			.orElseThrow(() ->
				new MemberNotFoundException(
					"삭제할 사용자를 찾을 수 없습니다.",
					"사용자를 삭제할 수 없음. id: " + withdrawalInfo.getMemberId()
				)
			);

		/*관리자는 다른 사용자 임의로 삭제 가능*/
		if (withdrawalInfo.getIsAdminDelete()) {
			return;
		}

		/*이메일로 회원가입한 경우 비밀번호 일치여부 검증*/
		if (member.isEmailLogin()) {
			if (!passwordEncoder.matches(withdrawalInfo.getPassword(), member.getPassword())) {
				throw new PasswordNotCorrectException(
					"비밀번호가 일치하지 않습니다.",
					"삭제 실패: id: " + withdrawalInfo.getMemberId()
				);
			}
		}
	}
}
