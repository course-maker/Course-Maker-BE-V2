package net.coursemaker.backendv2.course.command.domain.service.courseDestination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;
import net.coursemaker.backendv2.course.command.domain.repository.CourseDestinationRepository;

@ExtendWith(MockitoExtension.class)
class CourseDestinationQueryServiceTest {

    @Mock
    private CourseDestinationRepository courseDestinationRepository;

    @InjectMocks
    private CourseDestinationQueryService queryService;

    @Test
    void getCourseDestinations_성공() {
        // given
        Course course = createSampleCourse();
        List<CourseDestination> expectedDestinations = createSampleCourseDestinations();

        when(courseDestinationRepository.findAllByCourse(course))
            .thenReturn(expectedDestinations);

        // when
        List<CourseDestination> result = queryService.getCourseDestinations(course);

        // then
        assertThat(result).isEqualTo(expectedDestinations);
    }

    private Course createSampleCourse() {
        return Course.builder()
            .id(1L)
            .title("테스트 코스")
            .build();
    }

    private List<CourseDestination> createSampleCourseDestinations() {
        CourseDestination destination = CourseDestination.builder()
            .id(1L)
            .build();
        return List.of(destination);
    }
}
