package org.project.domain.user.service;

import java.time.Clock;
import org.project.domain.user.domain.User;
import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.repository.RefreshTokenRepository;
import org.project.util.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginCommonService {

  private final Clock clock;

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final String accessSecret;
  private final String refreshSecret;
  private final Long accessExpireTimeInMilliSeconds;
  private final Long refreshExpireTimeInMilliSeconds;

  public LoginCommonService(
      Clock clock,
      RefreshTokenRepository refreshTokenRepository,
      JwtService jwtService,
      @Value("${jwt.access-secret}") String accessSecret,
      @Value("${jwt.refresh-secret}") String refreshSecret,
      @Value("${jwt.access-expiration}") Long accessExpireTimeInMilliSeconds,
      @Value("${jwt.refresh-expiration}") Long refreshExpireTimeInMilliSeconds) {
    this.clock = clock;
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtService = jwtService;
    this.accessSecret = accessSecret;
    this.refreshSecret = refreshSecret;
    this.accessExpireTimeInMilliSeconds = accessExpireTimeInMilliSeconds;
    this.refreshExpireTimeInMilliSeconds = refreshExpireTimeInMilliSeconds;
  }

  AuthTokens loginUser(User user) {
    throw new UnsupportedOperationException("loginUser() not implemented yet.");
    // TODO: access, refresh 토큰 발급

    // TODO: refresh 토큰 저장
  }
}
