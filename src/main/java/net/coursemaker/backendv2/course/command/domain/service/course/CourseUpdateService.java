package net.coursemaker.backendv2.course.command.domain.service.course;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.dto.AddCourseRequestDTO;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;
import net.coursemaker.backendv2.course.command.domain.service.courseDestination.CourseDestinationUpdateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseUpdateService {

	private final CourseRepository courseRepository;
	private final CourseDestinationUpdateService courseDestinationService;

	@Transactional
	public Course updateCourse(Long id, AddCourseRequestDTO request) {
		log.info("[Course] 여행 코스 업데이트 요청: ID={} ", id);

		Course travelCourse = courseRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));

		travelCourse.updateTitle(request.getTitle());
		travelCourse.updateContents(request.getContent());
		travelCourse.updateDuration(request.getDuration());
		travelCourse.updateRecommendedTravelerRange(request.getRecommendedTravelerRange());
		travelCourse.updatePictureLink(request.getPictureLink());

		courseDestinationService.updateCourseDestinations(request.getCourseDestinations(), travelCourse);

		return courseRepository.save(travelCourse);
	}
}
