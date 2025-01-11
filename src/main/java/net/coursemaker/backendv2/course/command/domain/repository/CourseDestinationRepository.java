package net.coursemaker.backendv2.course.command.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;

@Repository
public interface CourseDestinationRepository extends JpaRepository<CourseDestination, Long> {
	List<CourseDestination> findAllByCourse(Course travelCourse);

	void deleteAllByCourseId(Long travelCourseId);

	void deleteAllByCourseId(long travelCourseId);
}
