package net.coursemaker.backendv2.destination.command.domain.aggregate;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Embeddable
public class Location {

	private String location;

	private BigDecimal longitude;

	private BigDecimal latitude;

	public Location() {}

	// 모든 필드를 초기화하는 생성자
	public Location(String location, BigDecimal longitude, BigDecimal latitude) {
		this.location = location;
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
