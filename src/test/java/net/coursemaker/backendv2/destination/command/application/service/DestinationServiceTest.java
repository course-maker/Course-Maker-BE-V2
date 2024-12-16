package net.coursemaker.backendv2.destination.command.application.service;

import net.coursemaker.backendv2.destination.command.application.dto.RequestDto;
import net.coursemaker.backendv2.destination.command.application.dto.UpdateDto;
import net.coursemaker.backendv2.destination.command.application.exception.DestinationNotFoundException;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.repository.DestinationRepository;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DestinationServiceTest {

	@Mock
	private DestinationRepository destinationRepository;

	private DestinationService destinationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		destinationService = new DestinationService(destinationRepository);
	}

	@Test
	@DisplayName("정상적으로 여행지 생성")
	void 여행지_생성_성공() {
		// Given
		RequestDto requestDto = new RequestDto();
		requestDto.setName("New Destination");
		Member member = new Member;
		Destination destination = new Destination;

		when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

		// When
		Destination result = destinationService.save(requestDto, member);

		// Then
		assertNotNull(result);
		assertEquals("New Destination", result.getName());
		verify(destinationRepository, times(1)).save(any(Destination.class));
	}

	@Test
	@DisplayName("여행지 생성 실패 - 이름 없음")
	void 여행지_생성_실패_이름없음() {
		// Given
		RequestDto requestDto = new RequestDto();
		requestDto.setName(null);
		Member member = new Member;

		// When & Then
		assertThrows(DestinationNotFoundException.class, () -> destinationService.save(requestDto, member));
		verify(destinationRepository, never()).save(any(Destination.class));
	}

	@Test
	@DisplayName("정상적으로 여행지 수정")
	void 여행지_수정_성공() {
		// Given
		Long id = 1L;
		UpdateDto updateDto = new UpdateDto();
		updateDto.setName("Updated Destination");
		Member member = new Member;
		Destination existingDestination = new Destination(/* parameters for destination */);

		when(destinationRepository.findById(id)).thenReturn(Optional.of(existingDestination));
		when(destinationRepository.save(any(Destination.class))).thenReturn(existingDestination);

		// When
		Destination result = destinationService.update(id, updateDto, member);

		// Then
		assertNotNull(result);
		assertEquals("Updated Destination", result.getName());
		verify(destinationRepository, times(1)).findById(id);
		verify(destinationRepository, times(1)).save(existingDestination);
	}

	@Test
	@DisplayName("여행지 수정 실패 - ID 없음")
	void 여행지_수정_실패_ID없음() {
		// Given
		Long id = 1L;
		UpdateDto updateDto = new UpdateDto();
		updateDto.setName("Updated Destination");
		Member member = new Member;

		when(destinationRepository.findById(id)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(DestinationNotFoundException.class, () -> destinationService.update(id, updateDto, member));
		verify(destinationRepository, times(1)).findById(id);
		verify(destinationRepository, never()).save(any(Destination.class));
	}

	@Test
	@DisplayName("정상적으로 여행지 삭제")
	void 여행지_삭제_성공() {
		// Given
		Long id = 1L;

		when(destinationRepository.existsById(id)).thenReturn(true);
		doNothing().when(destinationRepository).deleteById(id);

		// When
		assertDoesNotThrow(() -> destinationService.deleteById(id));

		// Then
		verify(destinationRepository, times(1)).existsById(id);
		verify(destinationRepository, times(1)).deleteById(id);
	}

	@Test
	@DisplayName("여행지 삭제 실패 - ID 없음")
	void 여행지_삭제_실패_ID없음() {
		// Given
		Long id = 1L;

		when(destinationRepository.existsById(id)).thenReturn(false);

		// When & Then
		assertThrows(DestinationNotFoundException.class, () -> destinationService.deleteById(id));
		verify(destinationRepository, times(1)).existsById(id);
		verify(destinationRepository, never()).deleteById(id);
	}
}
