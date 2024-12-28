package net.coursemaker.backendv2.auth.token;

import java.util.Date;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@RedisHash(value = "refreshToken", timeToLive = 86400)// TTL: 24시간
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;
	private Date expiration;

	public RefreshToken(String token, Date expiration) {
		this.token = token;
		this.expiration = expiration;
	}
}
