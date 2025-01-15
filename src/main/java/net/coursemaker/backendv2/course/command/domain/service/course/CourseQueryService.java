package net.coursemaker.backendv2.course.command.domain.service.course;

import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseQueryService {

	private final CourseRepository courseRepository;

	public Course findById(Long id) {
		log.debug("[Course] ID로 여행 코스 조회 요청: ID={} ", id);
		return courseRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));
	}
}
