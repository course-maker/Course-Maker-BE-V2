package net.coursemaker.backendv2.like.command.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.coursemaker.backendv2.like.command.domain.aggregate.LikeTargetType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeCreateDto {
    private Long memberId;
    private LikeTargetType targetType;
    private Long targetId;
}
