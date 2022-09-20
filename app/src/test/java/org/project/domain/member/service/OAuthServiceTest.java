package org.project.domain.member.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.project.domain.member.domain.Member;
import org.project.domain.member.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OAuthServiceTest {

  // member repository stub
  private MemberRepository memberRepository = mock(MemberRepository.class);

  OAuthService oAuthService = new OAuthService(
      memberRepository,
      "dummy google client id",
      "dummy google client secret",
      "dummy google redirect uri",
      "access secret",
      "refresh secret",
      1000L,
      2000L
  );

  @Test
  @DisplayName("모든 파라미터가 올바르게 주어졌을 때 토큰을 생성한다")
  public void generateToken_whenEveryParamsProper_returnNotNullToken() {
    // given
    Member member = new Member(10L, "", "");
    Date exp = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
    Key key = Keys.hmacShaKeyFor(
        "12345678901234567890123456789012".getBytes(StandardCharsets.UTF_8));

    // when
    String token = oAuthService.generateToken(key, member, exp);

    // then
    Assertions.assertThat(token).isInstanceOf(String.class);
  }

  @Test
  @DisplayName("member가 null이면 generateToken은 IAE를 던진다")
  public void generateToken_givenNullMember_throwsNullPointerException() {
    // given
    Member member = null;
    Date exp = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
    Key key = Keys.hmacShaKeyFor(
        "refresh secret must be longer than 256 bitsssssssssssssssss".getBytes(
            StandardCharsets.UTF_8));

    // when & then
    Assertions.assertThatThrownBy(() -> oAuthService.generateToken(key, member, exp))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("key가 null이면 generateToken은 IAE를 던진다")
  public void generateToken_givenNullKey_throwsNullPointerException() {
    // given
    Member member = new Member(10L, "", "");
    Date exp = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
    Key key = null;

    // when & then
    Assertions.assertThatThrownBy(() -> oAuthService.generateToken(key, member, exp))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("exp가 null이면 generateToken은 IAE를 던진다")
  public void generateToken_givenNullExp_throwsNullPointerException() {
    // given
    Member member = new Member(10L, "", "");
    Date exp = null;
    Key key = Keys.hmacShaKeyFor(
        "refresh secret must be longer than 256 bitsssssssssssssssss".getBytes(
            StandardCharsets.UTF_8));

    // when & then
    Assertions.assertThatThrownBy(() -> oAuthService.generateToken(key, member, exp))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("유효한 파라미터가 들어왔을 때 반환된 토큰은 주어진 파라미터와 일치해야 한다")
  public void generateToken_returns_token_with_proper_sub_claim() {
    // given
    Member member = new Member(10L, "test@test.com", "google");
    Date exp = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
    Key key = Keys.hmacShaKeyFor(
        "refresh secret must be longer than 256 bitsssssssssssssssss".getBytes(
            StandardCharsets.UTF_8));

    // when
    String refreshToken = oAuthService.generateToken(key, member, exp);

    // then
    String subInToken;
    try {
      subInToken = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(refreshToken)
          .getBody()
          .getSubject();
    } catch (ExpiredJwtException e) {
      subInToken = e.getClaims().getSubject();
    }
    Long idInSub = Long.parseLong(subInToken);
    assertEquals(member.getId(), idInSub);
  }
}
