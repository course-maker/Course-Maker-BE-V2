package net.coursemaker.backendv2.like.command.domain.service;

import net.coursemaker.backendv2.like.command.domain.dto.LikeCreateDto;
import net.coursemaker.backendv2.like.command.domain.exception.DuplicateLikeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import net.coursemaker.backendv2.like.command.domain.aggregate.Like;
import net.coursemaker.backendv2.like.command.domain.aggregate.LikeTargetType;
import net.coursemaker.backendv2.like.command.domain.repository.LikeCommandRepository;

@Service
@RequiredArgsConstructor
public class LikeCommandService {
    private final LikeCommandRepository likeCommandRepository;

    @Transactional
    public void createLike(LikeCreateDto dto) {
        // 중복 좋아요 검증
        if (likeCommandRepository.existsByMemberIdAndTargetTypeAndTargetId(
            dto.getMemberId(), dto.getTargetType(), dto.getTargetId())) {
            throw new DuplicateLikeException("이미 좋아요를 누르셨습니다.", "좋아요 중복");
        }

        Like like = new Like(dto.getTargetType(), dto.getTargetId(), dto.getMemberId());
        likeCommandRepository.save(like);
    }

    @Transactional
    public void deleteLike(Long memberId, LikeTargetType targetType, Long targetId) {
        likeCommandRepository.deleteByMemberIdAndTargetTypeAndTargetId(
            memberId, targetType, targetId);
    }
}