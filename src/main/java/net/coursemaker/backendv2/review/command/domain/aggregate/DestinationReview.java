package net.coursemaker.backendv2.review.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.coursemaker.backendv2.common.BaseEntity;
import net.coursemaker.backendv2.review.command.domain.dto.RequestDestinationDTO;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "destination_review")
public class DestinationReview extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT", length = 4000)
	private String description;

	@Column(name = "memberId", nullable = false)
	private Long memberId;

	@Column(name = "rating")
	private Double rating;

	@Column(name = "destinationId", nullable = false)
	private Long destinationId;

	@ElementCollection
	@CollectionTable(name = "destination_review_pictures", joinColumns = @JoinColumn(name = "review_id"))
	@Column(name = "picture")
	private List<String> pictures;

	@Column(name = "recommend_count")
	private Integer recommendCount = 0;

	public DestinationReview(String title, String description, Long memberId, Long destinationId, Double rating, List<String> pictures) {
		this.title = title;
		this.description = description;
		this.memberId = memberId;
		this.destinationId = destinationId;
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
	public void update(RequestDestinationDTO request) {
		this.title = request.getTitle();
		this.description = request.getDescription();
		this.rating = request.getRating();
		this.pictures = request.getPictures() != null ? request.getPictures() : new ArrayList<>();
	}



}
