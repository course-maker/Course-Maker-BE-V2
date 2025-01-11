package net.coursemaker.backendv2.course.command.domain.service.course;

import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoursePictureService {

	private final CourseRepository courseRepository;

	public void updatePictureLink(Long courseId, String newPictureLink) {
		log.info("[Course] 코스의 대표사진 URL 변경 요청: courseId={}, newPictureLink={}", courseId, newPictureLink);
		Course travelCourse = courseRepository.findById(courseId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));
		travelCourse.updatePictureLink(newPictureLink);
		courseRepository.save(travelCourse);
		log.info("[Course] 코스의 대표사진 URL 변경 완료: courseId={}", courseId);
	}
}
