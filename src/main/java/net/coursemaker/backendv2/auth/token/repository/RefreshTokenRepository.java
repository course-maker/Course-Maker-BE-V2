package net.coursemaker.backendv2.auth.token.repository;

import org.springframework.data.repository.CrudRepository;

import net.coursemaker.backendv2.auth.token.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	Boolean existsByToken(String token);

	void deleteByToken(String token);
}
