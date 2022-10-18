package org.project.domain.user.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.domain.user.dto.GoogleAccessTokenResponse;
import org.project.domain.user.dto.GoogleUserInfoResponse;

@ExtendWith(MockitoExtension.class)
public class OAuthGrantServiceTest {

  @InjectMocks
  private OAuthGrantService oAuthGrantService;
  @Mock
  private WebClientRequestService webClientRequestService;
  
  @Test
  @DisplayName("getGoogleEmail은 code를 받아 GoogleAccessTokenResponse를 받아온다.")
  void getGoogleEmail_getGoogleAccessTokenResponse() {
    // given
    String code = "code";
    GoogleAccessTokenResponse googleAccessTokenResponse = new GoogleAccessTokenResponse();
    Mockito.when(webClientRequestService.getGoogleOAuthAccessTokenResponse(code))
        .thenReturn(googleAccessTokenResponse);
    String email = "email";
    GoogleUserInfoResponse googleUserInfoResponse = new GoogleUserInfoResponse();
    googleUserInfoResponse.setEmail(email);
    Mockito.when(webClientRequestService.getGoogleUserInfoResponse(googleAccessTokenResponse))
        .thenReturn(googleUserInfoResponse);

    // when
    oAuthGrantService.getGoogleEmail(code);

    // then
    Mockito.verify(webClientRequestService).getGoogleOAuthAccessTokenResponse(code);
    Mockito.verify(webClientRequestService).getGoogleUserInfoResponse(googleAccessTokenResponse);
  }
}
