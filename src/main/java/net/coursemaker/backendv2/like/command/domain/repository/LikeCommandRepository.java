package net.coursemaker.backendv2.like.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.coursemaker.backendv2.like.command.domain.aggregate.Like;
import net.coursemaker.backendv2.like.command.domain.aggregate.LikeTargetType;
import org.springframework.data.repository.query.Param;


public interface LikeCommandRepository extends JpaRepository<Like, Long> {

	@Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.memberId = :memberId AND l.targetType = :type AND l.targetId = :id")
	boolean existsLike(
		@Param("memberId") Long memberId,
		@Param("type") LikeTargetType type,
		@Param("id") Long id
	);

	@Modifying
	@Query("DELETE FROM Like l WHERE l.memberId = :memberId AND l.targetType = :type AND l.targetId = :id")
	void deleteLike(
		@Param("memberId") Long memberId,
		@Param("type") LikeTargetType type,
		@Param("id") Long id
	);
}
