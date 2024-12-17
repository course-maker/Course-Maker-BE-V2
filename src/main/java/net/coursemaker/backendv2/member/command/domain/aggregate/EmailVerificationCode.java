package net.coursemaker.backendv2.member.command.domain.aggregate;

import java.time.LocalDateTime;



import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "emailVerificationCode", timeToLive = 600)
public class EmailVerificationCode {
	@Id
	private Long id;
	private String email;
	private String verificationCode;
	private Boolean isVerified;

	private LocalDateTime sendAt;
	private LocalDateTime expiredAt;

	public boolean isExpired() {
		if(LocalDateTime.now().isAfter(expiredAt)) {
			return true;
		}
		return false;
	}

	public void verified(String code) {
		if(verificationCode.equals(code)) {
			isVerified = true;
		} else {
			isVerified = false;
		}
	}

	public boolean isVerifiedSuccess() {
		if(!isExpired() && this.isVerified) {
			return true;
		}
		return false;
	}

	public EmailVerificationCode(String email, String verificationCode) {
		this.email = email;
		this.verificationCode = verificationCode;

		this.isVerified = false;
		this.sendAt = LocalDateTime.now();
		this.expiredAt = LocalDateTime.now().plusMinutes(3L);
	}
}
