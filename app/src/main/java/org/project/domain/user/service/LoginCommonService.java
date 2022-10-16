package org.project.domain.user.service;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
  private final Long accessExpireTimeInSeconds;
  private final Long refreshExpireTimeInSeconds;
  private final Key accessKey;
  private final Key refreshKey;

  public LoginCommonService(
      Clock clock,
      RefreshTokenRepository refreshTokenRepository,
      JwtService jwtService,
      @Value("${jwt.access-secret}") String accessSecret,
      @Value("${jwt.refresh-secret}") String refreshSecret,
      @Value("${jwt.access-expiration}") Long accessExpireTimeInSeconds,
      @Value("${jwt.refresh-expiration}") Long refreshExpireTimeInSeconds) {
    this.clock = clock;
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtService = jwtService;
    this.accessExpireTimeInSeconds = accessExpireTimeInSeconds;
    this.refreshExpireTimeInSeconds = refreshExpireTimeInSeconds;
    byte[] accessSecretBytes = accessSecret.getBytes();
    this.accessKey = Keys.hmacShaKeyFor(accessSecretBytes);
    byte[] refreshSecretBytes = refreshSecret.getBytes();
    this.refreshKey = Keys.hmacShaKeyFor(refreshSecretBytes);
  }

  AuthTokens loginUser(User user) {
    throw new UnsupportedOperationException("loginUser() not implemented yet.");
    // TODO: access, refresh 토큰 발급

    // TODO: refresh 토큰 저장
  }

  String refreshAccessToken(String refreshToken) {

    // refresh 토큰 레포지토리에서 확인 (만료되면 레포지토리에 미존재)
    refreshTokenRepository.find(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token."));

    // refresh 토큰에서 유저 이메일 가져오기
    String email = jwtService.getTokenSub(refreshKey, refreshToken);

    // access 토큰 발급
    Date accessExp = Date.from(LocalDateTime.now(clock)
        .plusSeconds(accessExpireTimeInSeconds)
        .atZone(ZoneId.systemDefault()).toInstant());
    return jwtService.generateToken(accessKey, email, accessExp);
  }
}
