package net.coursemaker.backendv2.course.command.domain.service.course;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.dto.AddCourseRequestDTO;
import net.coursemaker.backendv2.course.command.domain.exception.IllegalCourseArgumentException;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;
import net.coursemaker.backendv2.course.command.domain.service.courseDestination.CourseDestinationModificationService;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.exception.MemberNotFoundException;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseCreationService {

	private final CourseRepository courseRepository;
	private final MemberCommandRepository commandRepository;
	private final CourseDestinationModificationService courseDestinationModificationService;

	@Transactional
	public Course createCourse(AddCourseRequestDTO request) {
		log.info("[Course] 새로운 여행 코스 생성 요청: {}", request);

		validateTitle(request.getTitle());

		Member member = commandRepository.findByNicknameAndDeletedAtIsNull(request.getNickname())
			.orElseThrow(() -> new MemberNotFoundException(
				"수정할 사용자를 찾을 수 없습니다.",
				"사용자를 수정할 수 없음. id: " + request.getNickname()
			));

		Course travelCourse = Course.builder()
			.title(request.getTitle())
			.contents(request.getContent())
			.duration(request.getDuration())
			.recommendedTravelerRange(request.getRecommendedTravelerRange())
			.pictureLink(request.getPictureLink())
			.author(member)
			.averageRating(request.getAverageRating() != null ? request.getAverageRating() : 0)
			.wishCount(0)
			.likeCount(0)
			.reviewCount(0)
			.build();

		travelCourse = courseRepository.save(travelCourse);
		log.info("[Course] 여행 코스 생성 완료: {}", travelCourse);

		courseDestinationModificationService.saveCourseDestinations(request.getCourseDestinations(), travelCourse);

		return travelCourse;
	}

	private void validateTitle(String title) {
		if (title.length() > 50) {
			log.warn("[Course] 코스 제목 길이 초과: {}", title.length());
			throw new IllegalCourseArgumentException("코스 제목은 50자를 넘길 수 없습니다.", "Title length is over 50");
		}
	}
}
