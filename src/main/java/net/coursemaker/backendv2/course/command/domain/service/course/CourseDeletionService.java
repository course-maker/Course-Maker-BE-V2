package net.coursemaker.backendv2.course.command.domain.service.course;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseDeletionService {

	private final CourseRepository courseRepository;

	@Transactional
	public void deleteCourse(Long id, String nickname) {
		log.info("[Course] 여행 코스 삭제 요청: ID={}, 닉네임={}", id, nickname);

		Course travelCourse = courseRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));

		if (!travelCourse.getAuthor().getNickname().equals(nickname)) {
			throw new IllegalArgumentException("권한이 없는 사용자입니다.");
		}

		travelCourse.setDeletedAt(LocalDateTime.now());
		courseRepository.save(travelCourse);
		log.info("[Course] 여행 코스 삭제 완료: ID={} ", id);
	}
}
