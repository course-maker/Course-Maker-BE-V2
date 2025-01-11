// package net.coursemaker.backendv2.course.command.domain.service.courseDestination;
//
// import org.springframework.stereotype.Service;
//
// import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;
// import net.coursemaker.backendv2.course.command.domain.dto.CourseDestinationResponse;
// import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;
// import net.coursemaker.backendv2.destination.command.domain.dto.DestinationDto;
// import net.coursemaker.backendv2.review.command.domain.service.DestinationReviewService;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class CourseDestinationResponseMapper {
//
// 	private final TagService tagService;
// 	private final DestinationReviewService destinationReviewService;
// 	private final DestinationWishService destinationWishService;
// 	private final DestinationLikeService destinationLikeService;
//
// 	public CourseDestinationResponse toResponse(CourseDestination courseDestination) {
// 		List<TagResponseDto> tags = tagService.findAllByDestinationId(courseDestination.getDestination().getId());
// 		Double averageRating = destinationReviewService.getAverageRating(courseDestination.getDestination().getId());
// 		Destination destination = courseDestination.getDestination();
// 		Boolean isApiData = destination.getIsApiData();
// 		Integer reviewCount = destinationReviewService.getReviewCount(destination.getId());
// 		Integer wishCount = destinationWishService.getDestinationWishCount(destination.getId());
// 		Integer likeCount = destinationLikeService.getDestinationLikeCount(destination.getId());
//
// 		Boolean isMyCourseDestination = loginedInfo != null && loginedInfo.getNickname().equals(destination.getMember().getNickname());
// 		Boolean isMyWishDestination = loginedInfo != null && destinationWishService.isDestinationWishedByUser(destination.getId(), loginedInfo.getNickname());
// 		Boolean isMyLikeDestination = loginedInfo != null && destinationLikeService.isDestinationLikedByUser(destination.getId(), loginedInfo.getNickname());
//
// 		// DestinationDto destinationDto = DestinationDto.toDto(courseDestination.getDestination(), tags, isApiData, averageRating, isMyCourseDestination, reviewCount, wishCount, likeCount, isMyWishDestination, isMyLikeDestination);
// 		return new CourseDestinationResponse(courseDestination, destinationDto);
// 	}
// }
