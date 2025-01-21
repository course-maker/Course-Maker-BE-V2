package net.coursemaker.backendv2.tag.command.domain.service;

import lombok.RequiredArgsConstructor;
import net.coursemaker.backendv2.tag.command.domain.aggregate.Tag;
import net.coursemaker.backendv2.tag.command.domain.dto.TagPostDto;
import net.coursemaker.backendv2.tag.command.domain.dto.TagResponseDto;
import net.coursemaker.backendv2.tag.command.domain.dto.TagUpdateDto;
import net.coursemaker.backendv2.tag.command.domain.exception.TagNotFoundException;
import net.coursemaker.backendv2.tag.command.domain.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

	private final TagRepository tagRepository;
	private final TagDomainService tagDomainService;

	/**
	 * 태그 생성
	 */
	public TagResponseDto createTag(TagPostDto tagPostDto) {
		// 태그 도메인 서비스에서 생성 로직 처리
		Tag tag = tagDomainService.createTag(tagPostDto.getName(), tagPostDto.getDescription());
		tag = tagRepository.save(tag);

		// TagResponseDto로 변환
		return new TagResponseDto(tag.getId(), tag.getName(), tag.getDescription());
	}

	/**
	 * 태그 조회 (ID로 검색)
	 */
	public TagResponseDto findById(Long id) {
		Tag tag = tagRepository.findById(id)
			.orElseThrow(() -> new TagNotFoundException("태그를 찾을 수 없습니다.", "태그 ID: " + id));

		// TagResponseDto로 변환
		return new TagResponseDto(tag.getId(), tag.getName(), tag.getDescription());
	}

	/**
	 * 태그 조회 (이름으로 검색)
	 */
	public TagResponseDto findByName(String name) {
		Tag tag = tagRepository.findByName(name)
			.orElseThrow(() -> new TagNotFoundException("태그를 찾을 수 없습니다.", "태그 이름: " + name));

		// TagResponseDto로 변환
		return new TagResponseDto(tag.getId(), tag.getName(), tag.getDescription());
	}

	/**
	 * 모든 태그 조회
	 */
	public List<TagResponseDto> findAllTags() {
		return tagRepository.findAll()
			.stream()
			.map(tag -> new TagResponseDto(tag.getId(), tag.getName(), tag.getDescription()))
			.collect(Collectors.toList());
	}

	/**
	 * 태그 수정
	 */
	public TagResponseDto updateTag(TagUpdateDto tagUpdateDto) {
		Tag tag = tagRepository.findById(tagUpdateDto.getId())
			.orElseThrow(() -> new TagNotFoundException("태그를 찾을 수 없습니다.", "태그 ID: " + tagUpdateDto.getId()));

		// 태그 도메인 서비스에서 수정 로직 처리
		tagDomainService.updateTag(tag, tagUpdateDto.getName(), tagUpdateDto.getDescription());
		tag = tagRepository.save(tag);

		// TagResponseDto로 변환
		return new TagResponseDto(tag.getId(), tag.getName(), tag.getDescription());
	}

	/**
	 * 태그 삭제
	 */
	public void deleteById(Long id) {
		if (!tagRepository.existsById(id)) {
			throw new TagNotFoundException("태그를 찾을 수 없습니다.", "태그 ID: " + id);
		}
		tagRepository.deleteById(id);
	}
}
