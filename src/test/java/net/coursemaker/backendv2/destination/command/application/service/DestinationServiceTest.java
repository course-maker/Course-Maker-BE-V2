package net.coursemaker.backendv2.destination.command.application.service;

import net.coursemaker.backendv2.destination.command.application.dto.LocationDto;
import net.coursemaker.backendv2.destination.command.application.dto.RequestDto;
import net.coursemaker.backendv2.destination.command.application.dto.UpdateDto;
import net.coursemaker.backendv2.destination.command.application.exception.DestinationNotFoundException;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.repository.DestinationRepository;
import net.coursemaker.backendv2.destination.command.domain.service.DestinationDomainService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
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
		DestinationDomainService domainService = new DestinationDomainService(destinationRepository);
		destinationService = new DestinationService(domainService);
	}

	@Test
	@DisplayName("정상적으로 여행지 생성")
	void 여행지_생성_성공() {
		// Given
		RequestDto requestDto = new RequestDto();
		requestDto.setName("New Destination");
		requestDto.setLocation(new LocationDto("Seoul", new BigDecimal("127.1234"), new BigDecimal("37.5678")));
		Destination destination = mock(Destination.class);

		when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

		// When
		Destination result = destinationService.createDestination(requestDto, 1L);

		// Then
		assertNotNull(result);
		verify(destinationRepository, times(1)).save(any(Destination.class));
	}

	@Test
	@DisplayName("여행지 생성 실패 - 이름 없음")
	void 여행지_생성_실패_이름없음() {
		// Given
		RequestDto requestDto = new RequestDto();
		requestDto.setName(null);

		// When & Then
		assertThrows(DestinationNotFoundException.class, () -> destinationService.createDestination(requestDto, 1L));
		verify(destinationRepository, never()).save(any(Destination.class));
	}

	@Test
	@DisplayName("정상적으로 여행지 수정")
	void 여행지_수정_성공() {
		// Given
		Long id = 1L;
		UpdateDto updateDto = new UpdateDto();
		updateDto.setName("Updated Destination");
		updateDto.setLocation(new LocationDto("Busan", new BigDecimal("129.0756"), new BigDecimal("35.1796")));
		Destination existingDestination = mock(Destination.class);

		when(destinationRepository.findById(id)).thenReturn(Optional.of(existingDestination));
		when(destinationRepository.save(any(Destination.class))).thenReturn(existingDestination);

		// When
		Destination result = destinationService.update(id, updateDto, 1L);

		// Then
		assertNotNull(result);
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

		when(destinationRepository.findById(id)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(DestinationNotFoundException.class, () -> destinationService.update(id, updateDto, 1L));
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
