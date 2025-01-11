package net.coursemaker.backendv2.member.command.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.coursemaker.backendv2.member.command.domain.dto.MemberBasicSignUpInfo;
import net.coursemaker.backendv2.member.command.domain.service.MemberSignUpService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberSignUpService memberSignUpService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody MemberBasicSignUpInfo info) {
		memberSignUpService.basicSignUp(info);

		return ResponseEntity.ok().build();
	}
}
