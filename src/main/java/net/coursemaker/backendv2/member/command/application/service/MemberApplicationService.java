package net.coursemaker.backendv2.member.command.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.coursemaker.backendv2.member.command.domain.dto.MemberBasicSignUpInfo;
import net.coursemaker.backendv2.member.command.domain.service.MemberSignUpService;
import net.coursemaker.backendv2.member.command.domain.service.MemberWithdrawalService;


import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberApplicationService {
	private final MemberSignUpService signUpService;
	private final MemberWithdrawalService withdrawalService;

	public void signUp(MemberBasicSignUpInfo signUpInfo) {

		signUpService.basicSignUp(signUpInfo);
	}
}
