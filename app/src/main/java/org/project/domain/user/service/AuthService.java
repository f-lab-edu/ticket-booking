package org.project.domain.user.service;

import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.repository.MemberRepository;
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
    throw new UnsupportedOperationException("loginWithGoogle() not implemented yet.");
  }

  public String refreshAccessToken(String refreshToken) {
    return loginCommonService.refreshAccessToken(refreshToken);
  }
}
