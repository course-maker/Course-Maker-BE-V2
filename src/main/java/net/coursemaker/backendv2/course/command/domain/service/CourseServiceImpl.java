package net.coursemaker.backendv2.course.command.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.dto.AddTravelCourseRequest;
import net.coursemaker.backendv2.course.command.domain.dto.UpdateTravelCourseRequest;
import net.coursemaker.backendv2.course.command.domain.repository.CourseDestinationRepository;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;
import net.coursemaker.backendv2.destination.command.domain.service.DestinationService;
import net.coursemaker.backendv2.util.CourseMakerPagination;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl extends 	CourseService{

	private final CourseDestinationRepository courseDestinationRepository;
	private final CourseRepository courseRepository;
	private final DestinationService destinationService;
	// private final TagService tagService;
	// private final MemberService memberService;

	@Override
	public Course save(AddTravelCourseRequest request) {
		return null;
	}

	@Override
	public CourseMakerPagination<Course> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public CourseMakerPagination<Course> getAllOrderByViewsDesc(Pageable pageable) {
		return null;
	}

	@Override
	public Course findById(Long id) {
		return null;
	}

	@Override
	public CourseMakerPagination<Course> findByTitleContaining(String title, Pageable pageable) {
		return null;
	}

	@Override
	public CourseMakerPagination<Course> findByMemberNickname(String nickname, Pageable pageable) {
		return null;
	}

	@Override
	public Course update(Long id, UpdateTravelCourseRequest request, String nickname) {
		return null;
	}

	@Override
	public void delete(Long id, String nickname) {

	}

	@Override
	public Course incrementViews(Long id) {
		return null;
	}
}
