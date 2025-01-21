package net.coursemaker.backendv2.tag.command.domain.service;

import lombok.RequiredArgsConstructor;
import net.coursemaker.backendv2.tag.command.domain.aggregate.Tag;
import net.coursemaker.backendv2.tag.command.domain.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagDomainService {

	private final TagRepository tagRepository;

	/**
	 * 태그 생성 로직 (도메인 수준에서 중복 검사)
	 */
	public Tag createTag(String name, String description) {
		if (tagRepository.existsByName(name)) {
			throw new IllegalStateException("이미 존재하는 태그입니다: " + name); // 예외는 서비스 계층에서 처리 가능
		}
		return new Tag(name, description);
	}

	/**
	 * 태그 수정 로직
	 */
	public Tag updateTag(Tag tag, String newName, String newDescription) {
		if (!tag.getName().equals(newName) && tagRepository.existsByName(newName)) {
			throw new IllegalStateException("이미 존재하는 태그 이름입니다: " + newName);
		}
		tag.update(newName, newDescription);
		return tag;
	}

	/**
	 * 태그 존재 여부 확인
	 */
	public Optional<Tag> findByName(String name) {
		return tagRepository.findByName(name);
	}
}
