package org.project.domain.member.service;

import org.project.domain.member.dto.AuthTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public AuthTokens loginWithGoogle(String code) {
    return new AuthTokens("accessToddken " + code, "refreshToken");
  }
}
