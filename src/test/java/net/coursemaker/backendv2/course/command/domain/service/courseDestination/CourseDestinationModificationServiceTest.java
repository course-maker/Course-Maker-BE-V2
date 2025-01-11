package net.coursemaker.backendv2.course.command.domain.service.courseDestination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;
import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;
import net.coursemaker.backendv2.course.command.domain.dto.AddCourseDestinationRequest;
import net.coursemaker.backendv2.course.command.domain.repository.CourseDestinationRepository;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
import net.coursemaker.backendv2.destination.command.domain.aggregate.Location;
import net.coursemaker.backendv2.destination.command.domain.service.DestinationDomainService;

@ExtendWith(MockitoExtension.class)
class CourseDestinationModificationServiceTest {

    @Mock
    private CourseDestinationRepository courseDestinationRepository;

    @Mock
    private DestinationDomainService destinationService;

    @InjectMocks
    private CourseDestinationModificationService modificationService;

    @Test
    void saveCourseDestinations_성공() {
        // given
        Course course = createSampleCourse();
        List<AddCourseDestinationRequest> requests = createSampleRequests();
        Destination destination = createSampleDestination();

        when(destinationService.findDestinationById(anyLong()))
            .thenReturn(destination);

        // when
        modificationService.saveCourseDestinations(requests, course);

        // then
        verify(courseDestinationRepository, times(requests.size())).save(any(CourseDestination.class));
    }

    @Test
    void updateCourseDestinations_성공() {
        // given
        Course course = createSampleCourse();
        List<AddCourseDestinationRequest> requests = createSampleRequests();
        Destination destination = createSampleDestination();

        when(destinationService.findDestinationById(anyLong()))
            .thenReturn(destination);

        // when
        modificationService.updateCourseDestinations(requests, course);

        // then
        verify(courseDestinationRepository).deleteAllByCourseId(course.getId());
        verify(courseDestinationRepository, times(requests.size())).save(any(CourseDestination.class));
    }

    private Course createSampleCourse() {
        return Course.builder()
            .id(1L)
            .title("테스트 코스")
            .build();
    }

    private List<AddCourseDestinationRequest> createSampleRequests() {
        AddCourseDestinationRequest request = new AddCourseDestinationRequest();
        // request 설정
        return List.of(request);
    }


	private Destination createSampleDestination() {
		Location location = new Location("테스트 주소", new BigDecimal("37.5665"), new BigDecimal("126.9780"));
		return new Destination(
			1L,                  // memberId
			"테스트 목적지",        // name
			"test_image.jpg",    // pictureLink
			"테스트 내용",         // content
			location,            // location
			4.5,                // averageRating
			false,              // isApiData
			null,               // contentId
			null,               // seq
			null                // apiContent
		);
	}
}
