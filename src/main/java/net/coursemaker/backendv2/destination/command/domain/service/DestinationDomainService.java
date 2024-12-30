package net.coursemaker.backendv2.destination.command.domain.service;

import org.springframework.stereotype.Service;

import java.util.List;

import net.coursemaker.backendv2.destination.command.domain.aggregate.Location;
import net.coursemaker.backendv2.destination.command.domain.exception.DestinationNotFoundException;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.exception.InvalidContentException;
import net.coursemaker.backendv2.destination.command.domain.exception.InvalidLocationException;
import net.coursemaker.backendv2.destination.command.domain.exception.InvalidNameException;
import net.coursemaker.backendv2.destination.command.domain.exception.InvalidPictureLinkException;
import net.coursemaker.backendv2.destination.command.domain.exception.InvalidTagException;
import net.coursemaker.backendv2.destination.command.domain.exception.UnauthorizedAccessException;
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
		// 유효성 검증
		validateName(destination.getName());
		validateLocation(destination.getLocation());
		validatePictureLink(destination.getPictureLink());
		validateContent(destination.getContent());

		// 저장
		destinationRepository.save(destination);
	}

	/**
	 * 여행지 이름 유효성 검증
	 */
	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new InvalidNameException(
				"여행지 이름은 공백일 수 없습니다.",
				"유효하지 않은 여행지 이름: null 또는 빈 문자열"
			);
		}
		if (name.length() > 30) {
			throw new InvalidNameException(
				"여행지 이름은 30글자를 초과할 수 없습니다.",
				"유효하지 않은 여행지 이름: 길이 초과 (" + name.length() + "글자)"
			);
		}
	}

	/**
	 * 위치 유효성 검증
	 */
	private void validateLocation(Location location) {
		if (location == null) {
			throw new InvalidLocationException(
				"여행지 위치 정보는 필수입니다.",
				"유효하지 않은 여행지 위치: null"
			);
		}
		if (location.getLocation() == null || location.getLocation().isBlank()) {
			throw new InvalidLocationException(
				"여행지 위치는 공백일 수 없습니다.",
				"유효하지 않은 위치: null 또는 빈 문자열"
			);
		}
		if (location.getLongitude() == null || location.getLatitude() == null) {
			throw new InvalidLocationException(
				"여행지의 위도와 경도는 필수입니다.",
				"유효하지 않은 위도/경도: null"
			);
		}
	}

	/**
	 * 대표 사진 유효성 검증
	 */
	private void validatePictureLink(String pictureLink) {
		if (pictureLink != null && !pictureLink.startsWith("http")) {
			throw new InvalidPictureLinkException(
				"대표 사진 링크는 유효한 URL이어야 합니다.",
				"유효하지 않은 대표 사진 링크: " + pictureLink
			);
		}
	}

	/**
	 * 텍스트 에디터 내용 유효성 검증
	 */
	private void validateContent(String content) {
		if (content != null && content.length() > 500) {
			throw new InvalidContentException(
				"여행지 설명은 500자를 초과할 수 없습니다.",
				"유효하지 않은 여행지 설명: 길이 초과 (" + content.length() + "자)"
			);
		}
	}

	/**
	 * ID로 여행지를 조회합니다.
	 * @param id 여행지 ID
	 * @return 여행지 엔티티
	 */
	public Destination findDestinationById(Long id) {
		return destinationRepository.findById(id)
			.orElseThrow(() -> new DestinationNotFoundException(
				"ID가 " + id + "인 여행지를 찾을 수 없습니다.",
				"여행지 조회 실패: ID=" + id
			));
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
		if (!existsDestinationById(id)) {
			throw new DestinationNotFoundException(
				"ID가 " + id + "인 여행지를 찾을 수 없습니다.",
				"여행지 삭제 실패: ID=" + id
			);
		}
		destinationRepository.deleteById(id);
	}

	/**
	 * 여행지에 태그를 추가하는 도메인 로직.
	 * @param destination 여행지 엔티티
	 * @param tags 추가할 태그 목록
	 */
	public void addTagsToDestination(Destination destination, List<Tag> tags) {
		if (tags == null || tags.isEmpty()) {
			throw new InvalidTagException(
				"태그 목록은 비어 있을 수 없습니다.",
				"유효하지 않은 태그 목록: null 또는 빈 리스트"
			);
		}
		for (Tag tag : tags) {
			validateTag(tag);
			destination.addTag(tag);
		}
	}

	/**
	 * 태그 유효성 검증
	 */
	private void validateTag(Tag tag) {
		if (tag.getName() == null || tag.getName().isBlank()) {
			throw new InvalidTagException(
				"태그 이름은 공백일 수 없습니다.",
				"유효하지 않은 태그 이름: null 또는 빈 문자열"
			);
		}
		if (tag.getName().length() > 20) {
			throw new InvalidTagException(
				"태그 이름은 20자를 초과할 수 없습니다.",
				"유효하지 않은 태그 이름: 길이 초과 (" + tag.getName().length() + "글자)"
			);
		}
	}

	public double calculateAverageRating(List<Integer> ratings) {
		if (ratings == null || ratings.isEmpty()) {
			return 0.0;
		}
		ratings.forEach(this::validateRating);
		return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
	}

	/**
	 * 리뷰 평점 유효성 검증
	 */
	private void validateRating(Integer rating) {
		if (rating == null || rating < 0 || rating > 5) {
			throw new InvalidRatingException(
				"리뷰 평점은 0에서 5 사이여야 합니다.",
				"유효하지 않은 평점: " + rating
			);
		}
	}

	/**
	 * 특정 사용자가 여행지를 수정할 권한이 있는지 확인.
	 * @param destination 여행지 엔티티
	 * @param userNickname 사용자 닉네임
	 * @return 수정 권한 여부
	 */
	public void ensureUserCanModifyDestination(Destination destination, String userNickname) {
		if (destination == null) {
			throw new DestinationNotFoundException(
				"여행지가 존재하지 않습니다.",
				"수정 권한 확인 실패: destination=null"
			);
		}
		if (!destination.getMember().getNickname().equals(userNickname)) {
			throw new UnauthorizedAccessException(
				"사용자는 해당 여행지를 수정할 권한이 없습니다.",
				"수정 권한 확인 실패: userNickname=" + userNickname + ", owner=" + destination.getMember().getNickname()
			);
		}
	}
}
