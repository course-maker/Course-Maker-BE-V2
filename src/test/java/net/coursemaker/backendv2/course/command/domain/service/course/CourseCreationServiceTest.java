package net.coursemaker.backendv2.course.command.domain.service.course;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.dto.AddCourseRequestDTO;
import net.coursemaker.backendv2.course.command.domain.exception.IllegalCourseArgumentException;
import net.coursemaker.backendv2.course.command.domain.repository.CourseRepository;
import net.coursemaker.backendv2.course.command.domain.service.courseDestination.CourseDestinationModificationService;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;
import net.coursemaker.backendv2.member.command.domain.repository.MemberCommandRepository;

@ExtendWith(MockitoExtension.class)
class CourseCreationServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MemberCommandRepository memberCommandRepository;

    @Mock
    private CourseDestinationModificationService courseDestinationModificationService;

    @InjectMocks
    private CourseCreationService courseCreationService;

    @Test
    void createCourse_성공() {
        // given
        AddCourseRequestDTO request = createSampleRequest();
        Member member = createSampleMember();
        Course course = createSampleCourse(member);

        when(memberCommandRepository.findByNicknameAndDeletedAtIsNull(anyString()))
            .thenReturn(Optional.of(member));
        when(courseRepository.save(any(Course.class)))
            .thenReturn(course);

        // when
        Course result = courseCreationService.createCourse(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
        verify(courseDestinationModificationService).saveCourseDestinations(any(), any());
    }

    @Test
    void createCourse_제목_50자_초과_실패() {
        // given
        AddCourseRequestDTO request = createRequestWithLongTitle();

        // when & then
        assertThatThrownBy(() -> courseCreationService.createCourse(request))
            .isInstanceOf(IllegalCourseArgumentException.class);
    }

	private AddCourseRequestDTO createSampleRequest() {
		AddCourseRequestDTO request = new AddCourseRequestDTO();
		request.setTitle("테스트 코스");
		request.setContent("테스트 내용");
		request.setNickname("tester");
		return request;
	}

	private AddCourseRequestDTO createRequestWithLongTitle() {
		AddCourseRequestDTO request = new AddCourseRequestDTO();
		request.setTitle("이것은_50자가_넘는_매우_긴_제목입니다_이것은_50자가_넘는_매우_긴_제목입니다_이것은_50자가_넘는_매우_긴_제목입니다");
		request.setContent("테스트 내용");
		request.setNickname("tester");
		return request;
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
