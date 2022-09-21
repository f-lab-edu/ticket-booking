package org.project.domain.member.service;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import org.project.domain.member.domain.Member;
import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.dto.KakaoUserInfoResponse;
import org.project.domain.member.dto.OAuthAccessTokenResponse;
import org.project.domain.member.dto.GoogleUserInfoResponse;
import org.project.domain.member.repository.MemberRepository;
import org.project.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final OAuthWebClientService oAuthWebClientService;
  private final JwtService jwtService;
  private final MemberRepository memberRepository;

  private final String googleClientId;
  private final String googleClientSecret;
  private final String googleRedirectUri;
  private final String kakaoClientId;
  private final String kakaoClientSecret;
  private final String kakaoRedirectUri;
  private final String accessSecret;
  private final String refreshSecret;
  private final Long accessExpiration;
  private final Long refreshExpiration;

  private final Map<String, Boolean> refreshTokens;

  OAuthService(
      OAuthWebClientService oAuthWebClientService,
      JwtService jwtService,
      MemberRepository memberRepository,
      Map<String, Boolean> refreshTokens,
      @Value("${oauth2.google.client-id}")
      String googleClientId,
      @Value("${oauth2.google.client-secret}")
      String googleClientSecret,
      @Value("${oauth2.google.redirect-uri}")
      String googleRedirectUri,
      @Value("${oauth2.kakao.client-id}")
      String kakaoClientId,
      @Value("${oauth2.kakao.client-secret}")
      String kakaoClientSecret,
      @Value("${oauth2.kakao.redirect-uri}")
      String kakaoRedirectUri,
      @Value("${jwt.access-secret}")
      String accessSecret,
      @Value("${jwt.refresh-secret}")
      String refreshSecret,
      @Value("${jwt.access-expiration}")
      Long accessExpiration,
      @Value("${jwt.refresh-expiration}")
      Long refreshExpiration
  ) {
    this.oAuthWebClientService = oAuthWebClientService;
    this.jwtService = jwtService;
    this.memberRepository = memberRepository;
    this.refreshTokens = refreshTokens;
    this.googleClientId = googleClientId;
    this.googleClientSecret = googleClientSecret;
    this.googleRedirectUri = googleRedirectUri;
    this.kakaoClientId = kakaoClientId;
    this.kakaoClientSecret = kakaoClientSecret;
    this.kakaoRedirectUri = kakaoRedirectUri;
    this.accessSecret = accessSecret;
    this.refreshSecret = refreshSecret;
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;

  }


  public AuthTokens loginWithGoogle(String code) {
    if (code == null) {
      throw new IllegalArgumentException("Authorization code is null");
    }

    // Get Google Email
    String email = getGoogleEmail(code);

    // Check if user exists
    Member memberInRepository = memberRepository.findByEmailAndProvider(email, "google");

    // Create user if not exists
    if (memberInRepository == null) {
      memberInRepository = memberRepository.save(new Member(email, "google"));
    }

    // Create access token
    String accessToken = jwtService.generateToken(
        Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8)),
        memberInRepository.getId().toString(),
        new Date(System.currentTimeMillis() + accessExpiration));

    // Create refresh token
    String refreshToken = jwtService.generateToken(
        Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8)),
        memberInRepository.getId().toString(),
        new Date(System.currentTimeMillis() + refreshExpiration));

    // Cache refresh token in in-memory cache
    refreshTokens.put(refreshToken, true);

    // Return access token and refresh token
    return new AuthTokens(accessToken, refreshToken);
  }

  public AuthTokens loginWithKakao(String code) {
    if (code == null) {
      throw new IllegalArgumentException("Authorization code is null");
    }

    // Get Google Email
    String email = getKakaoEmail(code);

    // Check if user exists
    Member memberInRepository = memberRepository.findByEmailAndProvider(email, "kakao");

    // Create user if not exists
    if (memberInRepository == null) {
      memberInRepository = memberRepository.save(new Member(email, "kakao"));
    }

    // Create access token
    String accessToken = jwtService.generateToken(
        Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8)),
        memberInRepository.getId().toString(),
        new Date(System.currentTimeMillis() + accessExpiration));

    // Create refresh token
    String refreshToken = jwtService.generateToken(
        Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8)),
        memberInRepository.getId().toString(),
        new Date(System.currentTimeMillis() + refreshExpiration));

    // Cache refresh token in in-memory cache
    refreshTokens.put(refreshToken, true);

    // Return access token and refresh token
    return new AuthTokens(accessToken, refreshToken);
  }

  private String getGoogleEmail(String code) {
    // Request google access token
    OAuthAccessTokenResponse response = oAuthWebClientService.getGoogleAccessToken(code,
        googleClientId, googleClientSecret, googleRedirectUri);

    // Request google user info
    String googleAccessToken = response.getAccessToken();
    GoogleUserInfoResponse userInfo = oAuthWebClientService.getGoogleUserInfo(googleAccessToken);

    // Get google email address from user info
    return userInfo.getEmail();
  }

  String getKakaoEmail(String code) {
    // Request kakao access token
    OAuthAccessTokenResponse response = oAuthWebClientService.getKakaoAccessToken(code,
        kakaoClientId, kakaoClientSecret, kakaoRedirectUri);

    // Request kakao user info
    String kakaoAccessToken = response.getAccessToken();
    KakaoUserInfoResponse userInfo = oAuthWebClientService.getKakaoUserInfo(kakaoAccessToken);

    // Get kakao email address from user info
    return userInfo.getKakaoAccount().getEmail();
  }


}
