package net.coursemaker.backendv2.member.command.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;




public interface MemberCommandRepository extends JpaRepository<Member, Long> {
	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByPhoneNumber(String phoneNumber);
}
