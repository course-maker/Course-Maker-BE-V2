package net.coursemaker.backendv2.course.command.domain.aggregate;

import net.coursemaker.backendv2.member.command.domain.aggregate.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String contents;
	private int views; //조회수
	private int duration; // 여행기간
	private int travelerCount;
	private Integer travelType;
	private String pictureLink;
	private Member member;
	private Double averageRating;
	private Integer wishCount;
	private Integer reviewCount;
	private Integer likeCount;




}
