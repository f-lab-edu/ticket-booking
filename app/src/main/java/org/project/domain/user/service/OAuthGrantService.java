package org.project.domain.user.service;

import org.project.domain.user.dto.GoogleAccessTokenResponse;
import org.project.domain.user.dto.GoogleUserInfoResponse;
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
