package net.coursemaker.backendv2.like.command.domain.aggregate;

import net.coursemaker.backendv2.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private LikeTargetType targetType;

	private Long targetId;

	private Long memberId;

	public Like(LikeTargetType targetType, Long targetId, Long memberId) {
		this.targetType = targetType;
		this.targetId = targetId;
		this.memberId = memberId;
	}

}
