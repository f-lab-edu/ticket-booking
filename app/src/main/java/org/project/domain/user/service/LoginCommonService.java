package org.project.domain.user.service;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.project.domain.user.domain.Member;
import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.repository.RefreshTokenRepository;
import org.project.util.JwtService;
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
    // TODO: Custom Exception 작성 후 변경
    if (!result) {
      throw new IllegalArgumentException("refresh token not found");
    }
  }
}
