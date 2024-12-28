package net.coursemaker.backendv2.auth.token;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TokenType {
	ACCESS_TOKEN("access"),
	REFRESH_TOKEN("refresh");

	private final String type;

	TokenType(String type) {
		this.type = type;
	}

}
