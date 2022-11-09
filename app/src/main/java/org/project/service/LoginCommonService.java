package org.project.service;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.project.domain.Member;
import org.project.dto.AuthTokens;
import org.project.exception.InvalidAccessTokenException;
import org.project.repository.RefreshTokenRepository;
import org.project.exception.InvalidRefreshTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(rollbackFor = Exception.class)
  AuthTokens loginMember(Member member) {

    // access, refresh 토큰 발급
    // JwtService에서 Date를 받기 때문에 변환 필요
    Date accessExp = Date.from(LocalDateTime.now(clock)
        .plusSeconds(accessExpireTimeInSeconds)
        .atZone(ZoneId.systemDefault()).toInstant());
    Date refreshExp = Date.from(LocalDateTime.now(clock)
        .plusSeconds(refreshExpireTimeInSeconds)
        .atZone(ZoneId.systemDefault()).toInstant());
    String accessJws = jwtService.generateToken(accessKey, member.getEmail(), accessExp);
    String refreshJws = jwtService.generateToken(refreshKey, member.getEmail(), refreshExp);
    AuthTokens authTokens = new AuthTokens(accessJws, refreshJws);

    // refresh 토큰 저장
    refreshTokenRepository.save(refreshJws);

    return authTokens;
  }

  @Transactional(rollbackFor = Exception.class)
  void logoutMember(String refreshToken) {
    Boolean result = refreshTokenRepository.delete(refreshToken);
    if (!result) {
      throw new InvalidRefreshTokenException();
    }
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

  public void validateAccessToken(String accessToken) {
    if (!jwtService.isTokenValid(accessKey, accessToken)) {
      throw new InvalidAccessTokenException();
    }
  }

  public String getSubFromAccessToken(String accessToken) {
    return jwtService.getTokenSub(accessKey, accessToken);
  }
}
