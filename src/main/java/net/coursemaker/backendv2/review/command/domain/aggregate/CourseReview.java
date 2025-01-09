package net.coursemaker.backendv2.review.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.coursemaker.backendv2.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "course_review")
public class CourseReview extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT", length = 4000)
	private String description;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "rating")
	private Double rating;

	@Column(name = "course_id", nullable = false)
	private Long courseId;

	@ElementCollection
	@CollectionTable(name = "course_review_pictures", joinColumns = @JoinColumn(name = "review_id"))
	@Column(name = "picture")
	private List<String> pictures;

	@Column(name = "recommend_count")
	private Integer recommendCount = 0;

	public CourseReview(String title, String description, Long memberId, Long courseId, Double rating, List<String> pictures) {
		this.title = title;
		this.description = description;
		this.memberId = memberId;
		this.courseId = courseId;
		this.rating = rating;
		this.pictures = pictures != null ? pictures : new ArrayList<>();
	}

	public void markAsDeleted() {
		this.setDeletedAt(LocalDateTime.now());
	}

	public void addRecommendation(Long memberId) {
		this.recommendCount += 1;
	}

	public void removeRecommendation(Long memberId) {
		this.recommendCount = Math.max(0, this.recommendCount - 1);
	}

	public void update(String title, String description, Double rating, List<String> pictures) {
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.pictures = pictures != null ? pictures : new ArrayList<>();
	}
}
