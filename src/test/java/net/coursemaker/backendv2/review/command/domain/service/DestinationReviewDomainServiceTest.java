package net.coursemaker.backendv2.review.command.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReview;
import net.coursemaker.backendv2.review.command.domain.aggregate.DestinationReviewRecommendation;
import net.coursemaker.backendv2.review.command.domain.dto.RequestDestinationDTO;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewAlreadyRecommendedException;
import net.coursemaker.backendv2.review.command.domain.exception.ReviewPermissionDeniedException;
import net.coursemaker.backendv2.review.command.domain.repository.DestinationReviewRepository;

class DestinationReviewDomainServiceTest {

	@InjectMocks
	private DestinationReviewDomainService destinationReviewDomainService;

	@Mock
	private DestinationReviewRepository destinationReviewRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 리뷰_생성_저장_성공() {
		RequestDestinationDTO 요청 = new RequestDestinationDTO("멋진 장소", "놀라운 풍경!", 4.5, List.of("사진1", "사진2"));
		DestinationReview 리뷰 = new DestinationReview("멋진 장소", "놀라운 풍경!", 1L, 1L, 4.5, List.of("사진1", "사진2"));

		when(destinationReviewRepository.save(any(DestinationReview.class))).thenReturn(리뷰);

		DestinationReview 저장된리뷰 = destinationReviewDomainService.createReview(요청, 1L, 1L);

		assertNotNull(저장된리뷰);
		assertEquals("멋진 장소", 저장된리뷰.getTitle());
		verify(destinationReviewRepository, times(1)).save(any(DestinationReview.class));
	}

	@Test
	void 리뷰_수정_성공() {
		RequestDestinationDTO 요청 = new RequestDestinationDTO("수정된 제목", "수정된 설명", 4.0, List.of("사진3"));
		DestinationReview 리뷰 = new DestinationReview("멋진 장소", "놀라운 풍경!", 1L, 1L, 4.5, List.of("사진1", "사진2"));

		when(destinationReviewRepository.findById(1L)).thenReturn(Optional.of(리뷰));
		when(destinationReviewRepository.save(any(DestinationReview.class))).thenReturn(리뷰);

		DestinationReview 수정된리뷰 = destinationReviewDomainService.updateReview(1L, 요청, 1L);

		assertNotNull(수정된리뷰);
		assertEquals("수정된 제목", 수정된리뷰.getTitle());
		verify(destinationReviewRepository, times(1)).save(리뷰);
	}

	@Test
	void 리뷰_수정_권한_없음() {
		RequestDestinationDTO 요청 = new RequestDestinationDTO("수정된 제목", "수정된 설명", 4.0, List.of("사진3"));
		DestinationReview 리뷰 = new DestinationReview("멋진 장소", "놀라운 풍경!", 2L, 1L, 4.5, List.of("사진1", "사진2"));

		when(destinationReviewRepository.findById(1L)).thenReturn(Optional.of(리뷰));

		assertThrows(ReviewPermissionDeniedException.class, () -> destinationReviewDomainService.updateReview(1L, 요청, 1L));
	}

	@Test
	void 리뷰_삭제_성공() {
		DestinationReview 리뷰 = new DestinationReview("멋진 장소", "놀라운 풍경!", 1L, 1L, 4.5, List.of("사진1", "사진2"));

		when(destinationReviewRepository.findById(1L)).thenReturn(Optional.of(리뷰));

		destinationReviewDomainService.deleteReview(1L, 1L);

		assertNotNull(리뷰.getDeletedAt());
		verify(destinationReviewRepository, times(1)).save(리뷰);
	}

	@Test
	void 추천_추가_성공() {
		DestinationReview 리뷰 = new DestinationReview("멋진 장소", "놀라운 풍경!", 1L, 1L, 4.5, List.of("사진1", "사진2"));

		when(destinationReviewRepository.findById(1L)).thenReturn(Optional.of(리뷰));
		when(destinationReviewRepository.findRecommendation(1L, 1L)).thenReturn(Optional.empty());

		destinationReviewDomainService.addRecommendation(1L, 1L);

		assertEquals(1, 리뷰.getRecommendCount());
		verify(destinationReviewRepository, times(1)).save(리뷰);
	}

	@Test
	void 추천_이미_존재() {
		DestinationReview 리뷰 = new DestinationReview("멋진 장소", "놀라운 풍경!", 1L, 1L, 4.5, List.of("사진1", "사진2"));
		DestinationReviewRecommendation 추천 = new DestinationReviewRecommendation(1L, 1L);

		when(destinationReviewRepository.findById(1L)).thenReturn(Optional.of(리뷰));
		when(destinationReviewRepository.findRecommendation(1L, 1L)).thenReturn(Optional.of(추천));

		assertThrows(
			ReviewAlreadyRecommendedException.class, () -> destinationReviewDomainService.addRecommendation(1L, 1L));
	}

	@Test
	void 회원_리뷰_조회_성공() {
		Pageable 페이지요청 = PageRequest.of(0, 10);
		List<DestinationReview> 리뷰목록 = List.of(new DestinationReview("멋진 장소", "놀라운 풍경!", 1L, 1L, 4.5, List.of("사진1")));
		Page<DestinationReview> 페이지 = new PageImpl<>(리뷰목록, 페이지요청, 리뷰목록.size());

		when(destinationReviewRepository.findByMemberId(1L, 페이지요청)).thenReturn(페이지);

		Page<DestinationReview> 결과 = destinationReviewDomainService.findReviewsByMember(1L, 페이지요청);

		assertNotNull(결과);
		assertEquals(1, 결과.getContent().size());
		verify(destinationReviewRepository, times(1)).findByMemberId(1L, 페이지요청);
	}
}

