package net.coursemaker.backendv2.course.command.domain.service.courseDestination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;
import net.coursemaker.backendv2.course.command.domain.dto.AddCourseDestinationRequest;
import net.coursemaker.backendv2.course.command.domain.dto.UpdateCourseDestinationRequest;
import net.coursemaker.backendv2.course.command.domain.repository.CourseDestinationRepository;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.service.DestinationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseDestinationUpdateService {

	private final CourseDestinationRepository courseDestinationRepository;
	private final DestinationService destinationService;

	@Transactional
	public void updateCourseDestinations(List<AddCourseDestinationRequest> courseDestinations, Course travelCourse) {
		log.info("[CourseDestination] 여행 코스 목적지 업데이트 요청: travelCourseId={}", travelCourse.getId());

		// 기존 목적지 삭제
		deleteExistingDestinations(travelCourse);

		// 새로운 목적지 추가
		addNewDestinations(courseDestinations, travelCourse);

		log.info("[CourseDestination] 여행 코스 목적지 업데이트 완료: travelCourseId={}", travelCourse.getId());
	}

	private void deleteExistingDestinations(Course travelCourse) {
		courseDestinationRepository.deleteAllByCourseId(travelCourse.getId());
		log.debug("[CourseDestination] 기존 목적지 삭제 완료: travelCourseId={}", travelCourse.getId());
	}

	private void addNewDestinations(List<AddCourseDestinationRequest> courseDestinations, Course travelCourse) {
		for (AddCourseDestinationRequest request : courseDestinations) {
			// Destination destination = destinationService.findById(request.getDestinationId())
			// 	.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 목적지입니다."));
			//
			// CourseDestination courseDestination = CourseDestination.builder()
			// 	.travelCourse(travelCourse)
			// 	.destination(destination)
			// 	.date(request.getDate())
			// 	.visitOrder(request.getVisitOrder())
			// 	.build();

			// courseDestinationRepository.save(courseDestination);
			// log.debug("[CourseDestination] 새로운 목적지 추가 완료: {}", courseDestination);
		}
	}
}
