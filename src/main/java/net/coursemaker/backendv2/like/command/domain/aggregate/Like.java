package net.coursemaker.backendv2.like.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.coursemaker.backendv2.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_type")
    @Enumerated(EnumType.STRING)
    private LikeTargetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "member_id")
    private Long memberId;

    public Like(LikeTargetType targetType, Long targetId, Long memberId) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.memberId = memberId;
    }

}
