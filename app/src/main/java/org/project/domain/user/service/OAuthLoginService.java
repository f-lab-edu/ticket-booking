package org.project.domain.user.service;

import org.project.domain.user.domain.Member;
import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthLoginService {

  private final MemberRepository memberRepository;
  private final OAuthGrantService oAuthGrantService;
  private final LoginCommonService loginCommonService;

  public OAuthLoginService(MemberRepository memberRepository, OAuthGrantService oAuthGrantService,
      LoginCommonService loginCommonService) {
    this.memberRepository = memberRepository;
    this.oAuthGrantService = oAuthGrantService;
    this.loginCommonService = loginCommonService;
  }

  public AuthTokens loginWithGoogle(String code) {

    String email = oAuthGrantService.getGoogleEmail(code);

    Member member = memberRepository.findByEmailAndProvider(email, "google").orElseGet(
        () -> memberRepository.save(Member.builder()
            .email(email)
            .provider("google")
            .build()));

    return loginCommonService.loginUser(member);
  }
}
