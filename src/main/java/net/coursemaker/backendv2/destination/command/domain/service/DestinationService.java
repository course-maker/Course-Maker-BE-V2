package net.coursemaker.backendv2.destination.command.domain.service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.*;

import net.coursemaker.backendv2.destination.command.domain.dto.RequestDto;
import net.coursemaker.backendv2.destination.command.domain.dto.UpdateDto;
import net.coursemaker.backendv2.destination.command.domain.exception.DestinationNotFoundException;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DestinationService {

	private final DestinationDomainService destinationDomainService;

	public DestinationService(DestinationDomainService destinationDomainService) {
		this.destinationDomainService = destinationDomainService;
	}

	/**
	 * 여행지 생성
	 * @param request 여행지 생성 요청 DTO
	 * @param memberId 여행지를 생성하는 회원 ID
	 * @return 저장된 여행지 엔티티
	 */
	@Transactional
	public Destination createDestination(RequestDto request, Long memberId) {

		if (request.getName() == null || request.getName().isBlank()) {
			throw new DestinationNotFoundException("ID가 " + id + "인 여행지를 찾을 수 없습니다.", "여행지 조회 실패: ID=" + id);
		}

		// DTO를 엔티티로 변환
		Destination destination = request.toEntity(memberId);

		// 도메인 서비스 호출로 저장
		destinationDomainService.saveDestination(destination);
		return destination;
	}

	/**
	 * 여행지 수정
	 * @param id     수정할 여행지 ID
	 * @param update 수정 요청 DTO
	 * @param memberId 여행지를 수정하는 회원 ID
	 * @return 수정된 여행지 엔티티
	 */
	@Transactional
	public Destination update(Long id, UpdateDto update, Long memberId) {
		// 도메인 서비스에서 여행지 조회
		Destination existingDestination = destinationDomainService.findDestinationById(id);

		// DTO를 통해 엔티티 업데이트
		update.toUpdate(existingDestination, memberId);

		// 도메인 서비스 호출로 저장
		destinationDomainService.saveDestination(existingDestination);
		return existingDestination;
	}

	/**
	 * 여행지 삭제
	 * @param id 삭제할 여행지 ID
	 */
	@Transactional
	public void deleteById(Long id) {
		// 여행지 존재 여부 확인 및 삭제 처리
		if (!destinationDomainService.existsDestinationById(id)) {
			throw new DestinationNotFoundException("ID가 " + id + "인 여행지를 찾을 수 없습니다.", "여행지 조회 실패: ID=" + id);
		}
		destinationDomainService.deleteDestinationById(id);
	}
}
