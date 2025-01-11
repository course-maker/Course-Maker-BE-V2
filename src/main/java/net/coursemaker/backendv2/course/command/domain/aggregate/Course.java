package net.coursemaker.backendv2.course.command.domain.aggregate;

import net.coursemaker.backendv2.common.BaseEntity;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "title", nullable = false, length = 30)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT")
	private String contents;

	@Column(name = "views", nullable = false)
	private int views; //조회수

	@Column(name = "duration")
	private int duration; // 여행기간

	@Column(name = "travelerCount")
	private int recommendedTravelerRange; // 여행추천인원

	@Column(name = "pictureLink", length = 300)
	private String pictureLink;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member author;

	@Column(name = "averageRating", nullable = false)
	private Double averageRating;

	@Column(name = "wishCount")
	private Integer wishCount; //찜

	@Column(name = "reviewCount")
	private Integer reviewCount; // 댓글수

	@Column(name = "likeCount")
	private Integer likeCount; //좋아요

	@Builder
	public Course(String title, String contents, int duration, int recommendedTravelerRange, String pictureLink, Member author, Double averageRating,
		Integer wishCount, Integer likeCount, Integer reviewCount) {
		this.title = title;
		this.contents = contents;
		this.views = 0;
		this.duration = duration;
		this.recommendedTravelerRange = recommendedTravelerRange;
		this.pictureLink = pictureLink;
		this.author = author;
		this.averageRating = averageRating;
		this.wishCount = wishCount;
		this.likeCount = likeCount;
		this.reviewCount = reviewCount;
	}

	// 변경 메서드
	public void updateTitle(String title) {
		if (title.length() > 50) {
			throw new IllegalArgumentException("코스 제목은 50자를 넘길 수 없습니다.");
		}
		this.title = title;
	}

	public void updateContents(String contents) {
		this.contents = contents;
	}

	public void updateDuration(int duration) {
		this.duration = duration;
	}

	public void updateRecommendedTravelerRange(int range) {
		this.recommendedTravelerRange = range;
	}

	public void updatePictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}

	public void incrementViews() {
		this.views++;
	}
}
