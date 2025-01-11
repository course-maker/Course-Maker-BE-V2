package net.coursemaker.backendv2.course.command.domain.aggregate;

import net.coursemaker.backendv2.destination.command.domain.aggregate.Destination;

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
public class CourseDestination {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "visitOrder", nullable = false)
	private short visitOrder; // 방문순서

	@Column(name = "date", nullable = false)
	private short date;

	@ManyToOne
	@JoinColumn(name = "courseId")
	private Course travelCourse;

	@ManyToOne
	@JoinColumn(name = "destinationId")
	private Destination destination;

	@Builder
	public CourseDestination(Long id, Course travelCourse, Destination destination, short date, short visitOrder) {
		this.id = id;
		this.travelCourse = travelCourse;
		this.destination = destination;
		this.date = date;
		this.visitOrder = visitOrder;
	}


}
