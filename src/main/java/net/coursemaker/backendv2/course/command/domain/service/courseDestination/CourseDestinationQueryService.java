package net.coursemaker.backendv2.course.command.domain.service.courseDestination;

import java.util.List;

import org.springframework.stereotype.Service;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;
import net.coursemaker.backendv2.course.command.domain.repository.CourseDestinationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseDestinationQueryService {
	private final CourseDestinationRepository courseDestinationRepository;

	public List<CourseDestination> getCourseDestinations(Course course) {
		return courseDestinationRepository.findAllByCourse(course);
	}
}
