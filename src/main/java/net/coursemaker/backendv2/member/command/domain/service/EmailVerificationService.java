package net.coursemaker.backendv2.member.command.domain.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.member.command.domain.aggregate.EmailVerificationCode;
import net.coursemaker.backendv2.member.command.domain.exception.VerificationCodeExpiredException;
import net.coursemaker.backendv2.member.command.domain.exception.VerificationCodeMisMatchException;
import net.coursemaker.backendv2.member.command.domain.exception.VerificationCodeNotSendException;
import net.coursemaker.backendv2.member.command.domain.repository.EmailVerificationRepository;
import net.coursemaker.backendv2.util.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

	private final EmailVerificationRepository emailVerificationRepository;
	private final EmailSender emailSender;

	public void sendEmailVerification(String email) {

		String validateCode = generateValidateCode();
		String title = "Course Maker 이메일 인증코드";
		String content =
			"<h1>CourseMaker 방문해주셔서 감사합니다.</h1><br><br>"
				+ "인증 코드는 <b>" + validateCode + "</b>입니다.<br>"
				+ "인증 코드를 바르게 입력해주세요.";

		emailSender.sendMail(email, title, content);

		/*이메일 재인증의 경우 초기화 시킴.*/
		emailVerificationRepository.findByEmail(email).ifPresent(
			emailCode -> {
				emailVerificationRepository.deleteById(emailCode.getId());
			});

		EmailVerificationCode emailCode = new EmailVerificationCode(email, validateCode);


		log.info("[Auth] 인증용 이메일 전송. 시간: {}", LocalDateTime.now());

		emailVerificationRepository.save(emailCode);
	}

	public void validateCode(String email, String code) {
		EmailVerificationCode verificationCode = emailVerificationRepository.findByEmail(email).orElseThrow(() ->
			new VerificationCodeNotSendException("인증 코드가 전송되지 않았습니다.", "이메일 인증코드 전송 안됨."));

		verificationCode.verified(code);

		if (verificationCode.isExpired()) {
			throw new VerificationCodeExpiredException(
				"이메일 인증 시간이 만료됬습니다.",
				"이메일 인증코드 만료. email: " + email);
		}

		if (!verificationCode.isVerifiedSuccess()) {
			throw new VerificationCodeMisMatchException(
				"인증 코드가 일치하지 않습니다.",
				"인증코드 불일치. email: " + email);
		}

		/*인증 완료됬으면 삭제함*/
		emailVerificationRepository.deleteById(verificationCode.getId());
	}


	private String generateValidateCode() {
		UUID uuid = UUID.randomUUID();

		return uuid.toString().split("-")[0];
	}

}
