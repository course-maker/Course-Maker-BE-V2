package net.coursemaker.backendv2.course.command.domain.aggregate;

import org.hibernate.annotations.ColumnDefault;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Course {

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

	@Column(name = "travelType")
	@ColumnDefault("0")
	private Integer travelType;

	@Column(name = "pictureLink", length = 300)
	private String pictureLink;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member author;

	@Column(name = "averageRating", nullable = false)
	private Double averageRating;

	@Column(name = "wishCount")
	private Integer wishCount;

	@Column(name = "reviewCount")
	private Integer reviewCount;

	@Column(name = "likeCount")
	private Integer likeCount;

}
