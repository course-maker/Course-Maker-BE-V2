package net.coursemaker.backendv2.course.command.domain.service;

import org.springframework.data.domain.Pageable;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.dto.AddTravelCourseRequest;
import net.coursemaker.backendv2.course.command.domain.dto.UpdateTravelCourseRequest;
import net.coursemaker.backendv2.util.CourseMakerPagination;

public interface CourseService {
	Course save(AddTravelCourseRequest request);
	CourseMakerPagination<Course> findAll(Pageable pageable);
	CourseMakerPagination<Course> getAllOrderByViewsDesc(Pageable pageable);
	Course findById(Long id);
	CourseMakerPagination<Course> findByTitleContaining(String title, Pageable pageable);
	CourseMakerPagination<Course> findByMemberNickname(String nickname, Pageable pageable);
	Course update(Long id, UpdateTravelCourseRequest request, String nickname);
	void delete(Long id, String nickname);
	Course incrementViews(Long id);
}
