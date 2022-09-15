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
      "refresh secret".toString(),
      1000L,
      2000L
  );

  @Test
  public void generateToken_returns_token_with_proper_exp_claim() {
    // given
    Member member = new Member(10L, "test@test.com", "google");
    Date exp = Date.from(Instant.parse("2021-01-01T00:00:00.00Z"));
    Key key = Keys.hmacShaKeyFor(
        "refresh secret must be longer than 256 bitsssssssssssssssss".getBytes(
            StandardCharsets.UTF_8));

    // when
    String refreshToken = oAuthService.generateToken(key, member, exp);

    // then
    Date expInToken;
    try {
      expInToken = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(refreshToken)
          .getBody()
          .getExpiration();
    } catch (ExpiredJwtException e) {
      expInToken = e.getClaims().getExpiration();
    }
    assertEquals(exp, expInToken);
  }

  @Test
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
