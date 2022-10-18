package org.project.domain.user.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import io.jsonwebtoken.security.Keys;
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
import org.project.domain.user.domain.Member;
import org.project.domain.user.dto.AuthTokens;
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


  @DisplayName("로그인 성공")
  @Test
  public void test_jwt_generation() {
    // given
    Key accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
    Key refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
    String email = "test@test.com";
    String accessToken = "testAccessToken";
    String refreshToken = "testRefreshToken";
    given(jwtService.generateToken(eq(accessKey), eq(email), any()))
        .willReturn(accessToken);
    given(jwtService.generateToken(eq(refreshKey), eq(email), any()))
        .willReturn(refreshToken);
    Member testMember = Member.builder()
        .email(email)
        .build();

    // when
    AuthTokens authTokens = loginCommonService.loginMember(testMember);

    // then
    Mockito.verify(jwtService, Mockito.times(1))
        .generateToken(eq(accessKey), eq(email), any());
    Mockito.verify(jwtService, Mockito.times(1))
        .generateToken(eq(refreshKey), eq(email), any());
    Mockito.verify(refreshTokenRepository, Mockito.times(1))
        .save(refreshToken);
    assertThat(authTokens.getAccess()).isEqualTo(accessToken);
    assertThat(authTokens.getRefresh()).isEqualTo(refreshToken);
  }

  @DisplayName("토큰 존재할 때 로그아웃 시 리프레시 토큰 레포지토리에서 토큰 삭제")
  @Test
  public void test_logout_success() {
    // given
    String refreshToken = "testRefreshToken";
    given(refreshTokenRepository.delete(refreshToken)).willReturn(true);

    // when
    loginCommonService.logoutMember(refreshToken);

    // then
    Mockito.verify(refreshTokenRepository, Mockito.times(1))
        .delete(refreshToken);
  }

  @DisplayName("토큰 존재하지 않을 때 로그아웃 시 리프레시 토큰 레포지토리에서 토큰 삭제")
  @Test
  public void test_logout_fail() {
    // given
    String refreshToken = "testRefreshToken";
    given(refreshTokenRepository.delete(refreshToken)).willReturn(false);

    // TODO: Custom Exception 작성 후 수정
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      // when
      loginCommonService.logoutMember(refreshToken);
    });
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
