package net.coursemaker.backendv2.course.command.domain.service.courseDestination;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;
import net.coursemaker.backendv2.course.command.domain.dto.AddCourseDestinationRequest;
import net.coursemaker.backendv2.course.command.domain.dto.UpdateCourseDestinationRequest;
import net.coursemaker.backendv2.course.command.domain.repository.CourseDestinationRepository;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.service.DestinationDomainService;
import net.coursemaker.backendv2.destination.command.domain.service.DestinationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseDestinationModificationService {

	private final CourseDestinationRepository courseDestinationRepository;
	private final DestinationDomainService destinationService;

	@Transactional
	public void saveCourseDestinations(List<AddCourseDestinationRequest> courseDestinations, Course travelCourse) {
		log.info("[CourseDestination] 여행 코스 목적지 저장 요청: travelCourseId={}", travelCourse.getId());

		for (AddCourseDestinationRequest request : courseDestinations) {
			Destination destination = destinationService.findDestinationById(request.getDestination().getId());

			CourseDestination courseDestination = CourseDestination.builder()
				.travelCourse(travelCourse)
				.destination(destination)
				.date(request.getDate())
				.visitOrder(request.getVisitOrder())
				.build();

			courseDestinationRepository.save(courseDestination);
			log.debug("[CourseDestination] 목적지 추가 완료: {}");
		}

		log.info("[CourseDestination] 여행 코스 목적지 저장 완료: travelCourseId={}", travelCourse.getId());
	}

	@Transactional
	public void updateCourseDestinations(List<AddCourseDestinationRequest> courseDestinations, Course travelCourse) {
		log.info("[CourseDestination] 여행 코스 목적지 업데이트 요청: travelCourseId={}", travelCourse.getId());

		// 기존 목적지 삭제
		courseDestinationRepository.deleteAllByCourseId(travelCourse.getId());
		log.debug("[CourseDestination] 기존 목적지 삭제 완료: travelCourseId={}", travelCourse.getId());

		// 새로운 목적지 추가
		saveCourseDestinations(courseDestinations, travelCourse);

		log.info("[CourseDestination] 여행 코스 목적지 업데이트 완료: travelCourseId={}", travelCourse.getId());
	}
}

