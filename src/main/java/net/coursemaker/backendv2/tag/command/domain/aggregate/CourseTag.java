package net.coursemaker.backendv2.tag.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "course_tag")
public class CourseTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;          // 고유 ID

	@Column(name = "course_id", nullable = false)
	private Long courseId;    // 코스 ID

	@Column(name = "tag_id", nullable = false)
	private Long tagId;       // 태그 ID

	public CourseTag(Long courseId, Long tagId) {
		this.courseId = courseId;
		this.tagId = tagId;
	}
}
