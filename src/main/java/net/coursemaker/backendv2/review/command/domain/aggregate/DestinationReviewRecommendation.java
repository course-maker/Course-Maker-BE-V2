package net.coursemaker.backendv2.review.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "destination_review_recommendation")
public class DestinationReviewRecommendation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "review_id", nullable = false)
	private Long reviewId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	// 사용자 정의 생성자
	public DestinationReviewRecommendation(Long reviewId, Long memberId) {
		this.reviewId = reviewId;
		this.memberId = memberId;
	}
}
