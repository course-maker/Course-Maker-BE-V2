package net.coursemaker.backendv2.member.command.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.dto.MemberEntity;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MemberUtils {

	private final MemberCommandRepository memberCommandRepository;

	public MemberEntity getMemberById(Long memberId, boolean isAdmin) {
		Member member = memberCommandRepository.findById(memberId).orElseThrow(() ->
			new MemberNotFoundException(
				"존재하지 않는 사용자 입니다.",
				"존재하지 않는 사용자. id: " + memberId));

		if (isAdmin) {
			return MemberEntity.fromAggregate(member);
		}
		/*관리자 이외에는 탈퇴한 회원은 조회 불가*/
		validateMemberIsDeleted(member);

		return MemberEntity.fromAggregate(member);
	}

	public List<MemberEntity> getMemberByEmail(String email, boolean isAdmin) {
		List<Member> members = memberCommandRepository.findAllByEmail(email);

		/*회원이 존재하지 않을때*/
		if (members.isEmpty()) {
			throw new MemberNotFoundException(
				"존재하지 않는 사용자 입니다.",
				"존재하지 않는 사용자. email: " + email);
		}

		if (isAdmin) {
			return members.stream()
				.map(MemberEntity::fromAggregate)
				.collect(Collectors.toList());
		}

		return members.stream()
			.filter(m -> m.getDeletedAt() == null)
			.findFirst()
			.map(MemberEntity::fromAggregate)
			.stream()
			.toList();
	}

	public List<MemberEntity> getMemberByNickname(String nickname, boolean isAdmin) {

		List<Member> members = memberCommandRepository.findAllByNickname(nickname);

		/*회원이 존재하지 않을때*/
		if (members.isEmpty()) {
			throw new MemberNotFoundException(
				"존재하지 않는 사용자 입니다.",
				"존재하지 않는 사용자. nickname: " + nickname);
		}


		if (isAdmin) {
			return members.stream()
				.map(MemberEntity::fromAggregate)
				.collect(Collectors.toList());
		}

		return members.stream()
			.filter(m -> m.getDeletedAt() == null)
			.findFirst()
			.map(MemberEntity::fromAggregate)
			.stream()
			.toList();
	}


	private void validateMemberIsDeleted(Member member) {
		if (member.getDeletedAt() != null) {
			throw new MemberNotFoundException(
				"존재하지 않는 사용자 입니다.",
				"탈퇴한 회원. id: " + member.getId());
		}
	}
}
