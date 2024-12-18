package net.coursemaker.backendv2.destination.command.domain.service;

import org.springframework.stereotype.Service;

import java.util.List;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.repository.DestinationRepository;

@Service
public class DestinationDomainService {

	private final DestinationRepository destinationRepository;

	public DestinationDomainService(DestinationRepository destinationRepository) {
		this.destinationRepository = destinationRepository;
	}

	/**
	 * 여행지를 저장합니다.
	 * @param destination 여행지 엔티티
	 */
	public void saveDestination(Destination destination) {
		destinationRepository.save(destination);
	}

	/**
	 * ID로 여행지를 조회합니다.
	 * @param id 여행지 ID
	 * @return 여행지 엔티티
	 */
	public Destination findDestinationById(Long id) {
		return destinationRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ID가 " + id + "인 여행지를 찾을 수 없습니다."));
	}

	/**
	 * ID로 여행지가 존재하는지 확인합니다.
	 * @param id 여행지 ID
	 * @return 존재 여부
	 */
	public boolean existsDestinationById(Long id) {
		return destinationRepository.existsById(id);
	}

	/**
	 * ID로 여행지를 삭제합니다.
	 * @param id 여행지 ID
	 */
	public void deleteDestinationById(Long id) {
		destinationRepository.deleteById(id);
	}

	/**
	 * 여행지에 태그를 추가하는 도메인 로직.
	 * @param destination 여행지 엔티티
	 * @param tags 추가할 태그 목록
	 */
	public void addTagsToDestination(Destination destination, List<Tag> tags) {
		if (tags == null || tags.isEmpty()) {
			throw new IllegalArgumentException("태그가 비어있을 수 없습니다.");
		}
		for (Tag tag : tags) {
			destination.addTag(tag);
		}
	}

	/**
	 * 여행지의 리뷰 평균 평점을 계산.
	 * @param ratings 리뷰 평점 목록
	 * @return 평균 평점
	 */
	public double calculateAverageRating(List<Integer> ratings) {
		if (ratings == null || ratings.isEmpty()) {
			return 0.0;
		}
		return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
	}

	/**
	 * 특정 사용자가 여행지를 수정할 권한이 있는지 확인.
	 * @param destination 여행지 엔티티
	 * @param userNickname 사용자 닉네임
	 * @return 수정 권한 여부
	 */
	public boolean canUserModifyDestination(Destination destination, String userNickname) {
		return destination.getMember().getNickname().equals(userNickname);
	}
}
