package org.project.service;

import org.project.configuration.OAuth2Properties;
import org.project.configuration.OAuth2Properties.Google;
import org.project.dto.GoogleAccessTokenErrorResponse;
import org.project.dto.GoogleAccessTokenResponse;
import org.project.dto.GoogleUserInfoErrorResponse;
import org.project.dto.GoogleUserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class WebClientRequestService {

  private final WebClient webClient;
  private final OAuth2Properties oAuth2Properties;

  public WebClientRequestService(
      WebClient webClient,
      OAuth2Properties oAuth2Properties
  ) {
    this.webClient = webClient;
    this.oAuth2Properties = oAuth2Properties;

  }

  public GoogleUserInfoResponse getGoogleUserInfoResponse(
      GoogleAccessTokenResponse googleAccessTokenResponse) {
    try {
      return webClient
          .get()
          .uri("https://www.googleapis.com/userinfo/v2/me")
          .header("Authorization", "Bearer " + googleAccessTokenResponse.getAccessToken())
          .retrieve()
          // TODO: Custom exception 정의 후 수정
          .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
              response -> response.bodyToMono(GoogleUserInfoErrorResponse.class)
                  .map(error -> new RuntimeException(error.getMessage())))
          .bodyToMono(GoogleUserInfoResponse.class)
          .block();
    } catch (WebClientResponseException e) {
      // TODO: Custom exception 정의 후 수정
      throw new RuntimeException(e);
    }
  }

  public GoogleAccessTokenResponse getGoogleOAuthAccessTokenResponse(String code) {
    Google googleProperties = this.oAuth2Properties.getGoogle();

    MultiValueMap<String, String> googleAccessTokenRequest = new LinkedMultiValueMap<>();
    googleAccessTokenRequest.add("code", code);
    googleAccessTokenRequest.add("client_id", googleProperties.getClientId());
    googleAccessTokenRequest.add("client_secret", googleProperties.getClientSecret());
    googleAccessTokenRequest.add("redirect_uri", googleProperties.getRedirectUri());
    googleAccessTokenRequest.add("grant_type", "authorization_code");

    try {
      return webClient
          .post()
          .uri("https://oauth2.googleapis.com/token")
          .header("Content-Type", "application/x-www-form-urlencoded")
          .body(BodyInserters.fromFormData(googleAccessTokenRequest))
          .retrieve()
          // TODO: Custom exception 정의 후 수정
          .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
              response -> response.bodyToMono(GoogleAccessTokenErrorResponse.class)
                  .flatMap(errorResponse -> Mono.error(
                      new RuntimeException(errorResponse.getErrorDescription()))))
          .bodyToMono(GoogleAccessTokenResponse.class)
          .block();
    } catch (WebClientResponseException e) {
      // 스펙으로 정의된 에러가 아닌 경우
      // TODO: Custom exception 정의 후 수정
      throw new RuntimeException(e.getResponseBodyAsString());
    }
  }
}
