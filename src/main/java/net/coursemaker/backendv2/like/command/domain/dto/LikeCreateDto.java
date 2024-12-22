package net.coursemaker.backendv2.like.command.domain.dto;

import net.coursemaker.backendv2.like.command.domain.aggregate.LikeTargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeCreateDto {
	private Long memberId;
	private LikeTargetType targetType;
	private Long targetId;
}
