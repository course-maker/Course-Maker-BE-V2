package net.coursemaker.backendv2.destination.command.application.service;

import net.coursemaker.backendv2.destination.command.application.dto.RequestDto;
import net.coursemaker.backendv2.destination.command.application.dto.UpdateDto;
import net.coursemaker.backendv2.destination.command.application.exception.DestinationNotFoundException;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.repository.DestinationRepository;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.ValidationException;

@Service
public class DestinationService {

	private final DestinationRepository destinationRepository;

	public DestinationService(DestinationRepository destinationRepository) {
		this.destinationRepository = destinationRepository;
	}

	/**
	 * 여행지 생성
	 * @param request 여행지 생성 요청 DTO
	 * @param member  여행지를 생성하는 회원 엔티티
	 * @return 저장된 여행지 엔티티
	 */
	@Transactional
	public Destination save(RequestDto request, Member member) {

		if (request.getName() == null || request.getName().isBlank()) {
			throw new DestinationNotFoundException("여행지 이름은 비어 있을 수 없습니다.");
		}

		// DTO를 엔티티로 변환
		Destination destination = request.toEntity(member);

		// 엔티티 저장
		return destinationRepository.save(destination);
	}

	/**
	 * 여행지 수정
	 * @param id     수정할 여행지 ID
	 * @param update 수정 요청 DTO
	 * @param member 여행지를 수정하는 회원 엔티티
	 * @return 수정된 여행지 엔티티
	 */
	@Transactional
	public Destination update(Long id, UpdateDto update, Member member) {
		// 여행지 존재 여부 확인
		Destination existingDestination = destinationRepository.findById(id)
			.orElseThrow(() -> new DestinationNotFoundException("ID가 " + id + "인 여행지를 찾을 수 없습니다."));

		// DTO를 통해 엔티티 업데이트
		update.toUpdate(existingDestination, member);

		// 엔티티 저장
		return destinationRepository.save(existingDestination);
	}

	/**
	 * 여행지 삭제
	 * @param id 삭제할 여행지 ID
	 */
	@Transactional
	public void deleteById(Long id) {
		// 여행지 존재 여부 확인
		if (!destinationRepository.existsById(id)) {
			throw new DestinationNotFoundException("ID가 " + id + "인 여행지를 찾을 수 없습니다.");
		}

		// 삭제 처리
		destinationRepository.deleteById(id);
	}
}
