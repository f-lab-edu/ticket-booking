package org.project.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
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
public class JJwtUtilTest {


  private static final String ACCESS_TOKEN_SECRET = "access_token_secret must be at least 256 bits for HS256 algorithm";
  private static final String REFRESH_TOKEN_SECRET = "refresh_token_secret must be at least 256 bits for HS256 algorithm";

  private static final Key accessKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());

  private static final Key refreshKey = Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes());

  private static final Date fakeFixedDate = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
  private JwtUtil jwtUtil = new JJwtUtil();


  @Test
  void generateToken반환값_not_empty_test() {
    final String token = jwtUtil.generateToken(accessKey, "test", fakeFixedDate);
    assertThat(token).isNotEmpty();
  }

  @Test
  void generateToken반환값_not_blank_test() {
    final String token = jwtUtil.generateToken(accessKey, "test", fakeFixedDate);
    assertThat(token).isNotBlank();
  }

  @Test
  void generateToken반환_토큰의_sub값_확인() {
    // given
    final String sub = "test";

    // when
    final String token = jwtUtil.generateToken(accessKey, sub, fakeFixedDate);

    // then
    final String tokenSub = jwtUtil.getTokenSub(accessKey, token);
    assertThat(tokenSub).isEqualTo(sub);
  }

  @Test
  void generateToken반환_토큰의_exp값_확인() {
    // given
    final Date exp = fakeFixedDate;

    // when
    final String token = jwtUtil.generateToken(accessKey, "test", exp);

    // then
    final Date tokenExp = jwtUtil.getTokenExpirationTime(accessKey, token);
    assertThat(tokenExp).isEqualTo(exp);
  }

  @Test
  void generateToken에_넘긴_키_값으로_토큰_서명_체크_통과_테스트() {
    // given
    final Key key = accessKey;

    // when
    final String token = jwtUtil.generateToken(key, "test", fakeFixedDate);

    // then
    final boolean isTokenSignValid = jwtUtil.isTokenSignValid(key, token);
    assertThat(isTokenSignValid).isTrue();
  }

  @Test
  void generateToken에_넘기지_않은_키_값으로_토큰_서명_체크_실패_테스트() {
    // given
    final Key key = accessKey;
    final Key wrongKey = refreshKey;

    // when
    final String token = jwtUtil.generateToken(key, "test", fakeFixedDate);

    // then
    final boolean isTokenSignValid = jwtUtil.isTokenSignValid(wrongKey, token);
    assertThat(isTokenSignValid).isFalse();
  }

  @Test
  void isDateValidForTokenExpClaim에_넘긴_date값이_토큰_exp_기준에_valid하면_true_반환() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:01Z")); // 만료 시간이 기준 시간보다 1초 뒤
    final Date dateBeforeExp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final String token = jwtUtil.generateToken(accessKey, "test", exp);

    // when
    final boolean isTokenExpirationBeforeDate = jwtUtil.isDateValidForTokenExpClaim(accessKey,
        token, dateBeforeExp);

    // then
    assertThat(isTokenExpirationBeforeDate).isTrue();
  }

  @Test
  void isDateValidForTokenExpClaim에_넘긴_date값이_토큰_exp_기준에_invalid하면_false_반환() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:00Z")); // 만료 시간이 기준 시간보다 1초 전
    final Date dateAfterExp = Date.from(Instant.parse("2021-01-01T00:00:01Z"));
    final String token = jwtUtil.generateToken(accessKey, "test", exp);

    // when
    final boolean isTokenExpirationBeforeDate = jwtUtil.isDateValidForTokenExpClaim(accessKey,
        token, dateAfterExp);

    // then
    assertThat(isTokenExpirationBeforeDate).isFalse();
  }

  @Test
  void isDateValidForTokenExpClaim에서_토큰_만료시간이_인자로_넘긴_date와_같으면_false_반환() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final Date dateEqualToExp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final String token = jwtUtil.generateToken(accessKey, "test", exp);

    // when
    final boolean isTokenExpirationBeforeDate = jwtUtil.isDateValidForTokenExpClaim(accessKey,
        token, dateEqualToExp);

    // then
    assertThat(isTokenExpirationBeforeDate).isFalse();
  }

  @Test
  void isDateValidForTokenExpClaim에_토큰이_아닌_스트링을_넘기면_IllegalArgumentException_발생() {
    // given
    final Date exp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final Date dateEqualToExp = Date.from(Instant.parse("2021-01-01T00:00:00Z"));
    final String token = "not a token";

    // when
    final Throwable throwable = catchThrowable(() -> jwtUtil.isDateValidForTokenExpClaim(accessKey,
        token, dateEqualToExp));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  void getTokenExpirationTime이_토큰의_exp값을_반환하는지_테스트() {
    // given
    final Date exp = fakeFixedDate;

    // when
    final String token = jwtUtil.generateToken(accessKey, "test", exp);

    // then
    final Date tokenExp = jwtUtil.getTokenExpirationTime(accessKey, token);
    assertThat(tokenExp).isEqualTo(exp);
  }

  @Test
  void getTokenExpirationTime에_jwt_토큰이_아닌_스트링을_넘기면_IllegalArgumentException_발생() {
    // given
    final String token = "not a token";

    // when
    final Throwable throwable = catchThrowable(() -> jwtUtil.getTokenExpirationTime(accessKey,
        token));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void getTokenSub에서_토큰의_sub값을_반환하는지_테스트() {
    // given
    final String sub = "test";

    // when
    final String token = jwtUtil.generateToken(accessKey, sub, fakeFixedDate);

    // then
    final String tokenSub = jwtUtil.getTokenSub(accessKey, token);
    assertThat(tokenSub).isEqualTo(sub);
  }

  @Test
  void getTokenSub에_jwt_토큰이_아닌_스트링을_넘기면_IllegalArgumentException_발생() {
    // given
    final String token = "not a token";

    // when
    final Throwable throwable = catchThrowable(() -> jwtUtil.getTokenSub(accessKey, token));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isTokenSignValid에_넘긴_key가_토큰_서명과_일치하는_경우_true_반환() {
    // given
    final Key key = accessKey;
    final String token = jwtUtil.generateToken(key, "test", fakeFixedDate);

    // when
    final boolean isTokenSignValid = jwtUtil.isTokenSignValid(key, token);

    // then
    assertThat(isTokenSignValid).isTrue();
  }

  @Test
  void isTokenSignValid에_넘긴_key가_토큰_서명과_일치하지_않는_경우_false_반환() {
    // given
    final Key key = accessKey;
    final Key wrongKey = refreshKey;
    final String token = jwtUtil.generateToken(key, "test", fakeFixedDate);

    // when
    final boolean isTokenSignValid = jwtUtil.isTokenSignValid(wrongKey, token);

    // then
    assertThat(isTokenSignValid).isFalse();
  }

  @Test
  void isTokenSignValid에_jwt가_아닌_string을_넘기면_IllegalArgumentException_발생() {
    // given
    final Key key = accessKey;
    final String token = "test";

    // when
    final Throwable throwable = catchThrowable(() -> jwtUtil.isTokenSignValid(key, token));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

}
