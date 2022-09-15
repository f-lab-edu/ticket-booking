package org.project.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for methods below.
 * <p>
 * String generateToken(Key key, String sub, Date expiration);
 * <p>
 * Date getTokenExpirationTime(Key key, String token);
 * <p>
 * String getTokenSub(Key key, String token);
 * <p>
 * boolean isTokenExpirationBeforeDate(Key key, String token, Date date);
 * <p>
 * boolean isTokenSignValid(Key key, String token);
 */
public class JJwtServiceTest {


  private static final String ACCESS_TOKEN_SECRET = "access_token_secret must be at least 256 bits for HS256 algorithm";
  private static final String REFRESH_TOKEN_SECRET = "refresh_token_secret must be at least 256 bits for HS256 algorithm";

  private static final Key accessKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());

  private static final Key refreshKey = Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes());

  private static final Date fakeFixedDate = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
  private JwtService jwtService = new JJwtService();


  @Test
  @DisplayName("generateToken() 반환값이 not empty 이어야 한다.")
  void generateToken_withValidKeyAndSubAndExpiration_shouldReturnNotEmpty() {
    final String token = jwtService.generateToken(accessKey, "test", fakeFixedDate);
    assertThat(token).isNotEmpty();
  }

  @Test
  @DisplayName("generateToken() 반환값이 not blank 이어야 한다.")
  void generateToken_withValidKeyAndSubAndExpiration_shouldReturnNotBlank() {
    final String token = jwtService.generateToken(accessKey, "test", fakeFixedDate);
    assertThat(token).isNotBlank();
  }

  @Test
  @DisplayName("generateToken() 반환 토큰의 sub값이 입력한 sub 값과 같아야 한다.")
  void generateToken_withValidKeyAndSubAndExpiration_shouldReturnTokenWithSameSub() {
    // given
    final String sub = "test";

    // when
    final String token = jwtService.generateToken(accessKey, sub, fakeFixedDate);

    // then
    final String tokenSub = jwtService.getTokenSub(accessKey, token);
    assertThat(tokenSub).isEqualTo(sub);
  }

  @Test
  @DisplayName("generateToken() 반환 토큰의 exp값이 입력한 exp 값과 같아야 한다.")
  void generateToken_withValidKeyAndSubAndExpiration_shouldReturnTokenWithSameExp() {
    // given
    final Date exp = fakeFixedDate;

    // when
    final String token = jwtService.generateToken(accessKey, "test", exp);

    // then
    final Date tokenExp = jwtService.getTokenExpirationTime(accessKey, token);
    assertThat(tokenExp).isEqualTo(exp);
  }

  @Test
  @DisplayName("generateToken() 반환 토큰의 서명이 입력한 키로 검증 통과되어야 한다.")
  void generateToken_withValidKeyAndSubAndExpiration_returnTokenWhichShouldBeValidatedByGivenKey() {
    // given
    final Key key = accessKey;

    // when
    final String token = jwtService.generateToken(key, "test", fakeFixedDate);

    // then
    final boolean isTokenSignValid = jwtService.isTokenSignValid(key, token);
    assertThat(isTokenSignValid).isTrue();
  }

  @Test
  @DisplayName("generateToken() 반환 토큰의 서명이 입력하지 않은 키로는 검증 실패해야 한다.")
  void generateToken_withValidKeyAndSubAndExpiration_returnTokenWhichShouldNotBeValidatedByNotGivenKey() {
    // given
    final Key key = accessKey;
    final Key wrongKey = refreshKey;

    // when
    final String token = jwtService.generateToken(key, "test", fakeFixedDate);

    // then
    final boolean isTokenSignValid = jwtService.isTokenSignValid(wrongKey, token);
    assertThat(isTokenSignValid).isFalse();
  }

  @Test
  @DisplayName("isDateValidForTokenExpClaim()에 넘긴 date시점에 토큰이 유효하면 true를 반환해야 한다.")
  void isDateValidForTokenExpClaim_withValidTokenAndDate_shouldReturnTrue() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:01Z")); // 만료 시간이 기준 시간보다 1초 뒤
    final Date dateBeforeExp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final String token = jwtService.generateToken(accessKey, "test", exp);

    // when
    final boolean isTokenExpirationBeforeDate = jwtService.isDateValidForTokenExpClaim(accessKey,
        token, dateBeforeExp);

    // then
    assertThat(isTokenExpirationBeforeDate).isTrue();
  }

  @Test
  @DisplayName("isDateValidForTokenExpClaim()에 넘긴 date시점에 토큰이 유효하지 않으면 false를 반환해야 한다.")
  void isDateValidForTokenExpClaim_withValidTokenAndDate_shouldReturnFalse() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:00Z")); // 만료 시간이 기준 시간보다 1초 전
    final Date dateAfterExp = Date.from(Instant.parse("2021-01-01T00:00:01Z"));
    final String token = jwtService.generateToken(accessKey, "test", exp);

    // when
    final boolean isTokenExpirationBeforeDate = jwtService.isDateValidForTokenExpClaim(accessKey,
        token, dateAfterExp);

    // then
    assertThat(isTokenExpirationBeforeDate).isFalse();
  }

  @Test
  @DisplayName("isDateValidForTokenExpClaim()에 넘긴 date시점이 토큰의 exp와 같으면 false를 반환해야 한다.")
  void isDateValidForTokenExpClaim_whenDateAndExpAreTheSame_shouldReturnFalseIfDateIsSameWithTokenExp() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final Date dateEqualToExp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final String token = jwtService.generateToken(accessKey, "test", exp);

    // when
    final boolean isTokenExpirationBeforeDate = jwtService.isDateValidForTokenExpClaim(accessKey,
        token, dateEqualToExp);

    // then
    assertThat(isTokenExpirationBeforeDate).isFalse();
  }

  @Test
  @DisplayName("isDateValidForTokenExpClaim()에 jwt 토큰이 아닌 문자열을 넘기면 IllegalArgumentException을 던진다.")
  void isDateValidForTokenExpClaim_withNotJwtToken_shouldThrowIllegalArgumentException() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final Date dateEqualToExp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final String token = "not a token";

    // when
    final Throwable throwable = catchThrowable(
        () -> jwtService.isDateValidForTokenExpClaim(accessKey,
            token, dateEqualToExp));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  @DisplayName("getTokenExpirationTime()에 넘긴 토큰의 exp를 반환해야 한다.")
  void getTokenExpirationTime_withValidToken_shouldReturnTokenExp() {
    // given
    final Date exp = fakeFixedDate;

    // when
    final String token = jwtService.generateToken(accessKey, "test", exp);

    // then
    final Date tokenExp = jwtService.getTokenExpirationTime(accessKey, token);
    assertThat(tokenExp).isEqualTo(exp);
  }

  @Test
  @DisplayName("getTokenExpirationTime()에 넘긴 토큰이 jwt 토큰이 아니면 IllegalArgumentException을 던진다.")
  void getTokenExpirationTime_withNotJwtToken_shouldThrowIllegalArgumentException() {
    // given
    final String token = "not a token";

    // when
    final Throwable throwable = catchThrowable(() -> jwtService.getTokenExpirationTime(accessKey,
        token));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("getTokenSub()에 넘긴 토큰의 sub를 반환해야 한다.")
  void getTokenSub_withValidToken_shouldReturnTokenSub() {
    // given
    final String sub = "test";

    // when
    final String token = jwtService.generateToken(accessKey, sub, fakeFixedDate);

    // then
    final String tokenSub = jwtService.getTokenSub(accessKey, token);
    assertThat(tokenSub).isEqualTo(sub);
  }

  @Test
  @DisplayName("getTokenSub()에 넘긴 토큰이 jwt 토큰이 아니면 IllegalArgumentException을 던진다.")
  void getTokenSub_withNotJwtToken_shouldThrowIllegalArgumentException() {
    // given
    final String token = "not a token";

    // when
    final Throwable throwable = catchThrowable(() -> jwtService.getTokenSub(accessKey, token));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("isTokenSignValid()에 넘긴 토큰의 서명이 주어진 키에 대해 유효하면 true를 반환해야 한다.")
  void isTokenSignValid_withValidTokenAndMatchedKey_shouldReturnTrue() {
    // given
    final Key key = accessKey;
    final String token = jwtService.generateToken(key, "test", fakeFixedDate);

    // when
    final boolean isTokenSignValid = jwtService.isTokenSignValid(key, token);

    // then
    assertThat(isTokenSignValid).isTrue();
  }

  @Test
  @DisplayName("isTokenSignValid()에 넘긴 토큰의 서명이 주어진 키에 대해 유효하지 않으면 false를 반환해야 한다.")
  void isTokenSignValid_withValidTokenAndNotMatchedKey_shouldReturnFalse() {
    // given
    final Key key = accessKey;
    final Key wrongKey = refreshKey;
    final String token = jwtService.generateToken(key, "test", fakeFixedDate);

    // when
    final boolean isTokenSignValid = jwtService.isTokenSignValid(wrongKey, token);

    // then
    assertThat(isTokenSignValid).isFalse();
  }

  @Test
  @DisplayName("isTokenSignValid()에 넘긴 토큰이 jwt 토큰이 아니면 IllegalArgumentException을 던진다.")
  void isTokenSignValid_withNotJwtToken_shouldThrowIllegalArgumentException() {
    // given
    final Key key = accessKey;
    final String token = "test";

    // when
    final Throwable throwable = catchThrowable(() -> jwtService.isTokenSignValid(key, token));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

}
