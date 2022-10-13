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
    loginCommonService.logoutUser(refreshToken);

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
      loginCommonService.logoutUser(refreshToken);
    });
  }

}
