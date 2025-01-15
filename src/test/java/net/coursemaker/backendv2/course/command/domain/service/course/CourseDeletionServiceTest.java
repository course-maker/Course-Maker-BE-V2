package net.coursemaker.backendv2.course.command.domain.service.course;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

@ExtendWith(MockitoExtension.class)
class CourseDeletionServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseDeletionService courseDeletionService;

    @Test
    void deleteCourse_성공() {
        // given
        Long courseId = 1L;
        String nickname = "tester";
        Course course = createSampleCourse(nickname);

        when(courseRepository.findByIdAndDeletedAtIsNull(courseId))
            .thenReturn(Optional.of(course));

        // when
        courseDeletionService.deleteCourse(courseId, nickname);

        // then
        verify(courseRepository).save(any(Course.class));
        assertThat(course.getDeletedAt()).isNotNull();
    }

    @Test
    void deleteCourse_존재하지_않는_코스_실패() {
        // given
        when(courseRepository.findByIdAndDeletedAtIsNull(anyLong()))
            .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> courseDeletionService.deleteCourse(1L, "tester"))
            .isInstanceOf(IllegalArgumentException.class);
    }

	private Member createSampleMember() {
		return new Member(
			"test@example.com",  // email
			"password123",       // password
			"테스터",             // name
			"tester",           // nickname
			"01012345678",      // phoneNumber
			false               // marketingAgree
		);
	}

	private Course createSampleCourse(Member member) {
		return Course.builder()
			.title("테스트 코스")
			.contents("테스트 내용")
			.author(member)
			.build();
	}
}
