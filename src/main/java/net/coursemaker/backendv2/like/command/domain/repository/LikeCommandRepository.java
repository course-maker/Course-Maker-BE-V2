package net.coursemaker.backendv2.like.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.coursemaker.backendv2.like.command.domain.aggregate.Like;
import net.coursemaker.backendv2.like.command.domain.aggregate.LikeTargetType;


public interface LikeCommandRepository extends JpaRepository<Like, Long> {

	boolean existsByMemberIdAndTargetTypeAndTargetId(Long memberId, LikeTargetType targetType, Long targetId);

	void deleteByMemberIdAndTargetTypeAndTargetId(Long memberId, LikeTargetType targetType, Long targetId);
}
