package net.coursemaker.backendv2;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import jakarta.annotation.PostConstruct;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class CoursemakerV2Application {

	public static void main(String[] args) {
		SpringApplication.run(CoursemakerV2Application.class, args);
	}

	/*기본 시간 설정*/
	@PostConstruct
	void timeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
