package org.project.domain.member.service;

import org.project.domain.member.dto.GoogleUserInfoResponse;
import org.project.domain.member.dto.KakaoUserInfoResponse;
import org.project.domain.member.dto.OAuthAccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OAuthWebClientService {

  WebClient webClient = WebClient.create();

  public GoogleUserInfoResponse getGoogleUserInfo(String accessToken) {
    return webClient
        .get()
        .uri("https://www.googleapis.com/userinfo/v2/me")
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .bodyToMono(GoogleUserInfoResponse.class)
        .block();
  }

  KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
    return webClient
        .get()
        .uri("https://kapi.kakao.com/v2/user/me")
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .bodyToMono(KakaoUserInfoResponse.class)
        .block();
  }

  OAuthAccessTokenResponse getGoogleAccessToken(String code, String clientId, String clientSecret,
      String redirectUri) {
    return webClient
        .post()
        .uri("https://oauth2.googleapis.com/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body(BodyInserters.fromFormData("code", code)
            .with("client_id", clientId)
            .with("client_secret", clientSecret)
            .with("redirect_uri", redirectUri)
            .with("grant_type", "authorization_code"))
        .retrieve()
        .bodyToMono(OAuthAccessTokenResponse.class)
        .block();
  }

  OAuthAccessTokenResponse getKakaoAccessToken(String code, String clientId, String clientSecret,
      String redirectUri) {
    return webClient
        .post()
        .uri("https://kauth.kakao.com/oauth/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body(BodyInserters.fromFormData("code", code)
            .with("client_id", clientId)
            .with("client_secret", clientSecret)
            .with("redirect_uri", redirectUri)
            .with("grant_type", "authorization_code"))
        .retrieve()
        .bodyToMono(OAuthAccessTokenResponse.class)
        .block();
  }

}
