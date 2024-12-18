package net.coursemaker.backendv2.destination.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import net.coursemaker.backendv2.common.BaseEntity;
import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Destination extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "memberId")
	private Long memberId;

	@Column(name = "name", length = 30)
	private String name;

	@Column(name = "pictureLink", length = 300)
	private String pictureLink;

	@Column(name = "views")
	private int views = 0;

	@Column(name = "content", columnDefinition = "MEDIUMTEXT")
	private String content;

	@Column(name = "apiContent", columnDefinition = "TEXT")
	private String apiContent;

	@Embedded
	private Location location;

	@ElementCollection
	@Column(name = "tag_id")
	private List<Long> tagIds;

	@Column(name = "averageRating", nullable = false)
	private Double averageRating;

	@Column(name = "disabled")
	private Boolean disabled;

	// tourApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.
	@Column(name = "contentId")
	private Long contentId;

	// busanApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.
	@Column(name = "seq")
	private Integer seq;

	@Column(name = "isApiData")
	@ColumnDefault("false")
	private Boolean isApiData;

	@Column(name = "wishCount")
	private Integer wishCount;

	@Column(name = "reviewCount")
	private Integer reviewCount;

	@Column(name = "likeCount")
	private Integer likeCount;

	public Destination(Long memberId, String name, String url, String content, Location locationEntity, double v, boolean b, Long contentId, Integer seq, String apiContent) {
		super();
	}

	public void incrementViews() {
		this.views += 1;
	}

	// Soft delete 처리
	public void softDelete() {
		this.setDeletedAt(LocalDateTime.now());
	}

	public void update(Long memberId, String name, String pictureLink, String content, Location location,
		Double averageRating, Boolean disabled, Long contentId, Integer seq) {
		this.memberId = memberId;
		this.name = name;
		this.pictureLink = pictureLink;
		this.content = content;
		this.location = location;
		this.averageRating = averageRating;
		this.disabled = disabled;
		this.contentId = contentId;
		this.seq = seq;
	}
}
