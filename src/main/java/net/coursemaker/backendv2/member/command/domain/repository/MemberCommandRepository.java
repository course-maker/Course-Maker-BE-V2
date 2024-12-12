package net.coursemaker.backendv2.member.command.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;




public interface MemberCommandRepository extends JpaRepository<Member, Long> {
	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

	Optional<Member> findByEmailAndDeletedAtIsNull(String email);

	Optional<Member> findByNicknameAndDeletedAtIsNull(String nickname);

	Optional<Member> findByPhoneNumberAndDeletedAtIsNull(String phoneNumber);

}
