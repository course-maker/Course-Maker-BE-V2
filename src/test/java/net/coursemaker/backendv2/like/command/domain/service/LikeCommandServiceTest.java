package net.coursemaker.backendv2.like.command.domain.service;

import net.coursemaker.backendv2.like.command.domain.aggregate.Like;
import net.coursemaker.backendv2.like.command.domain.aggregate.LikeTargetType;
import net.coursemaker.backendv2.like.command.domain.dto.LikeCreateDto;
import net.coursemaker.backendv2.like.command.domain.exception.DuplicateLikeException;
import net.coursemaker.backendv2.like.command.domain.repository.LikeCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LikeCommandServiceTest {

	@Mock
	private LikeCommandRepository likeCommandRepository;

	private LikeCommandService likeCommandService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		likeCommandService = new LikeCommandService(likeCommandRepository);
	}

	@Test
	@DisplayName("좋아요 생성 성공")
	void createLike_Success() {

		// Given
		LikeCreateDto dto = new LikeCreateDto(1L, LikeTargetType.COURSE, 1L);
		when(likeCommandRepository.existsByMemberIdAndTargetTypeAndTargetId(1L, LikeTargetType.COURSE, 1L))
			.thenReturn(false);

		// When
		assertDoesNotThrow(() -> likeCommandService.createLike(dto));

		// Then
		verify(likeCommandRepository, times(1)).save(any(Like.class));
	}

	@Test
	@DisplayName("좋아요 중복 생성 실패")
	void createLike_DuplicateFail() {
		// Given
		LikeCreateDto dto = new LikeCreateDto(1L, LikeTargetType.COURSE, 1L);
		when(likeCommandRepository.existsByMemberIdAndTargetTypeAndTargetId(1L, LikeTargetType.COURSE, 1L))
			.thenReturn(true);

		// When & Then
		assertThrows(DuplicateLikeException.class, () -> likeCommandService.createLike(dto));
		verify(likeCommandRepository, never()).save(any(Like.class));
	}

	@Test
	@DisplayName("좋아요 삭제 성공")
	void deleteLike_Success() {
		// Given
		Long memberId = 1L;
		LikeTargetType targetType = LikeTargetType.COURSE;
		Long targetId = 1L;

		// When
		assertDoesNotThrow(() ->
			likeCommandService.deleteLike(memberId, targetType, targetId));

		// Then
		verify(likeCommandRepository, times(1))
			.deleteByMemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId);
	}

	@Test
	@DisplayName("여행지 좋아요 생성 성공")
	void createDestinationLike_Success() {
		// Given
		LikeCreateDto dto = new LikeCreateDto(1L, LikeTargetType.DESTINATION, 1L);
		when(likeCommandRepository.existsByMemberIdAndTargetTypeAndTargetId(1L, LikeTargetType.DESTINATION, 1L))
			.thenReturn(false);

		// When
		assertDoesNotThrow(() -> likeCommandService.createLike(dto));

		// Then
		verify(likeCommandRepository, times(1)).save(any(Like.class));
	}
}
