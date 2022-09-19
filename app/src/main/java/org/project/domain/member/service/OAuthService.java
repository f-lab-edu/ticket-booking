package org.project.domain.member.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.project.domain.member.domain.Member;
import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.dto.OAuthAccessTokenResponse;
import org.project.domain.member.dto.GoogleUserInfoResponse;
import org.project.domain.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OAuthService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final WebClient webClient = WebClient.create();

  private final MemberRepository memberRepository;

  private final String googleClientId;
  private final String googleClientSecret;
  private final String googleRedirectUri;
  private final String accessSecret;
  private final String refreshSecret;
  private final Long accessExpiration;
  private final Long refreshExpiration;

  private final Map<String, Boolean> refreshTokens = new ConcurrentHashMap<>();

  OAuthService(
      MemberRepository memberRepository,
      @Value("${oauth2.google.client-id}")
      String googleClientId,
      @Value("${oauth2.google.client-secret}")
      String googleClientSecret,
      @Value("${oauth2.google.redirect-uri}")
      String googleRedirectUri,
      @Value("${jwt.access-secret}")
      String accessSecret,
      @Value("${jwt.refresh-secret}")
      String refreshSecret,
      @Value("${jwt.access-expiration}")
      Long accessExpiration,
      @Value("${jwt.refresh-expiration}")
      Long refreshExpiration
  ) {
    this.memberRepository = memberRepository;
    this.googleClientId = googleClientId;
    this.googleClientSecret = googleClientSecret;
    this.googleRedirectUri = googleRedirectUri;
    this.accessSecret = accessSecret;
    this.refreshSecret = refreshSecret;
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;

  }


  public AuthTokens loginWithGoogle(String code) {

    // Get Google Email
    String email = getGoogleEmail(code);

    // Check if user exists
    Member memberInRepository = memberRepository.findByEmailAndProvider(email, "google");

    // Create user if not exists
    if (memberInRepository == null) {
      memberInRepository = memberRepository.save(new Member(email, "google"));
    }

    // Create access token
    String accessToken = generateToken(
        Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8)), memberInRepository,
        new Date(System.currentTimeMillis() + accessExpiration));

    // Create refresh token
    String refreshToken = generateToken(
        Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8)), memberInRepository,
        new Date(System.currentTimeMillis() + refreshExpiration));

    // Cache refresh token in in-memory cache
    refreshTokens.put(refreshToken, true);

    // Return access token and refresh token
    return new AuthTokens(accessToken, refreshToken);
  }

  private String getGoogleEmail(String code) {
    // Request google access token
    OAuthAccessTokenResponse response = getGoogleAccessToken(code);

    // Request google user info
    String googleAccessToken = response.getAccessToken();
    GoogleUserInfoResponse userInfo = getGoogleUserInfo(googleAccessToken);

    // Get google email address from user info
    String email = userInfo.getEmail();
    return email;
  }

  String generateToken(Key key, Member memberInRepository, Date exp) {
    return Jwts.builder()
        .setSubject(memberInRepository.getId().toString())
        .setExpiration(exp)
        .signWith(key)
        .compact();
  }

  private GoogleUserInfoResponse getGoogleUserInfo(String accessToken) {
    return webClient
        .get()
        .uri("https://www.googleapis.com/userinfo/v2/me")
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .bodyToMono(GoogleUserInfoResponse.class)
        .block();
  }

  private OAuthAccessTokenResponse getGoogleAccessToken(String code) {
    return webClient
        .post()
        .uri("https://oauth2.googleapis.com/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body(BodyInserters.fromFormData("code", code)
            .with("client_id", googleClientId)
            .with("client_secret", googleClientSecret)
            .with("redirect_uri", googleRedirectUri)
            .with("grant_type", "authorization_code"))
        .retrieve()
        .bodyToMono(OAuthAccessTokenResponse.class)
        .block();
  }
}
