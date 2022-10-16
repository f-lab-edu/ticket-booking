package org.project.domain.user.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.domain.user.repository.RefreshTokenRepository;
import org.project.util.JwtService;

@ExtendWith(MockitoExtension.class)
public class LoginCommonServiceTest {


  private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneId.of("UTC"));
  private final RefreshTokenRepository refreshTokenRepository = Mockito.mock(
      RefreshTokenRepository.class);
  private final JwtService jwtService = Mockito.mock(JwtService.class);
  private final String accessSecret = "access-secretaccess-secretaccess-secretaccess-secretaccess-secret";
  private final String refreshSecret = "refresh-secretrefresh-secretrefresh-secretrefresh-secretrefresh-secret";
  private final Long accessExpireTimeInSeconds = 3600L;
  private final Long refreshExpireTimeInSeconds = 86400L;

  private final LoginCommonService loginCommonService;

  LoginCommonServiceTest() {
    loginCommonService = new LoginCommonService(
        clock,
        refreshTokenRepository,
        jwtService,
        accessSecret,
        refreshSecret,
        accessExpireTimeInSeconds,
        refreshExpireTimeInSeconds
    );
  }

  @Test
  @DisplayName("레포지토리에 토큰이 없으면 예외를 던진다.")
  void refreshAccessToken_noTokenInRepository() {
    // given
    String refreshToken = "refresh-token";
    given(refreshTokenRepository.find(refreshToken)).willReturn(Optional.empty());

    // when
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> loginCommonService.refreshAccessToken(refreshToken));
  }

  @Test
  @DisplayName("레포지토리에 토큰이 존재하면 jwtService.getTokenSub를 refresh 키와 토큰으로 호출한다.")
  void refreshAccessToken_tokenInRepository() {
    // given
    String refreshToken = "refresh-token";
    given(refreshTokenRepository.find(refreshToken)).willReturn(Optional.of(1L));
    String testEmail = "test@test.com";
    given(jwtService.getTokenSub(any(Key.class), eq(refreshToken))).willReturn(testEmail);
    String accessToken = "access-token";
    given(jwtService.generateToken(any(Key.class), eq(testEmail), any(Date.class)))
        .willReturn(accessToken);
    // when
    loginCommonService.refreshAccessToken(refreshToken);

    // then
    Mockito.verify(jwtService)
        .getTokenSub(any(Key.class), eq(refreshToken));
    Mockito.verify(jwtService).generateToken(
        any(Key.class),
        eq(testEmail),
        any(Date.class)
    );
  }
}
