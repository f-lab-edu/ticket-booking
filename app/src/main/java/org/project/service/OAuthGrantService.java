package org.project.service;

import org.project.dto.GoogleAccessTokenResponse;
import org.project.dto.GoogleUserInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class OAuthGrantService {

  private final WebClientRequestService webClientRequestService;

  public OAuthGrantService(WebClientRequestService webClientRequestService) {
    this.webClientRequestService = webClientRequestService;
  }

  String getGoogleEmail(String code) {
    GoogleAccessTokenResponse googleAccessTokenResponse = webClientRequestService.getGoogleOAuthAccessTokenResponse(
        code);

    GoogleUserInfoResponse googleUserInfoResponse = webClientRequestService.getGoogleUserInfoResponse(
        googleAccessTokenResponse);

    if (googleUserInfoResponse.getEmail() == null) {
      // TODO: Custom exception 정의 후 수정
      throw new RuntimeException("Google email is null");
    }
    return googleUserInfoResponse.getEmail();
  }

}
