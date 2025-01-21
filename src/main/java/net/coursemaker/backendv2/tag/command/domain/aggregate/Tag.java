package net.coursemaker.backendv2.tag.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.coursemaker.backendv2.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag")
public class Tag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	public Tag(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
