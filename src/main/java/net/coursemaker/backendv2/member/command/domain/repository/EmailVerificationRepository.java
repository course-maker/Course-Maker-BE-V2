package net.coursemaker.backendv2.member.command.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import net.coursemaker.backendv2.member.command.domain.aggregate.EmailVerificationCode;



public interface EmailVerificationRepository extends CrudRepository<EmailVerificationCode, Long> {
	Optional<EmailVerificationCode> findByEmail(String email);
}
