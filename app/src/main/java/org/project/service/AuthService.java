package org.project.service;

import org.project.domain.Member;
import org.project.dto.AuthTokens;
import org.project.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final MemberRepository memberRepository;
  private final OAuthGrantService oAuthGrantService;
  private final LoginCommonService loginCommonService;

  public AuthService(MemberRepository memberRepository, OAuthGrantService oAuthGrantService,
      LoginCommonService loginCommonService) {
    this.memberRepository = memberRepository;
    this.oAuthGrantService = oAuthGrantService;
    this.loginCommonService = loginCommonService;
  }

  public AuthTokens loginWithGoogle(String code) {

    String email = oAuthGrantService.getGoogleEmail(code);

    Member member = memberRepository.findByEmailAndProvider(email, "google").orElseGet(
        () -> memberRepository.save(new Member(email, "google")));

    return loginCommonService.loginMember(member);
  }

  public void logout(String refreshToken) {
    loginCommonService.logoutMember(refreshToken);
  }

  public String refreshAccessToken(String refreshToken) {
    return loginCommonService.refreshAccessToken(refreshToken);
  }
}
