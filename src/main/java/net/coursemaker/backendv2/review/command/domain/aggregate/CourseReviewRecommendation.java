package net.coursemaker.backendv2.review.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "course_review_recommendation")
public class CourseReviewRecommendation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "review_id", nullable = false)
	private Long reviewId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	public CourseReviewRecommendation(Long reviewId, Long memberId) {
		this.reviewId = reviewId;
		this.memberId = memberId;
	}
}
