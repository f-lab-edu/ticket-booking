package org.project.controller;

import javax.validation.Valid;
import org.project.dto.AuthTokens;
import org.project.dto.OAuthLoginQueryParameter;
import org.project.dto.OAuthLoginResponse;
import org.project.dto.AuthLogoutRequest;
import org.project.service.AuthService;
import org.project.exception.OAuthCodeRequestFailException;
import org.project.dto.TokenRefreshRequest;
import org.project.dto.TokenRefreshResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/google")
  @ResponseStatus(HttpStatus.OK)
  public OAuthLoginResponse loginWithGoogle(OAuthLoginQueryParameter request) {
    if (request.getError() != null) {
      throw new OAuthCodeRequestFailException(
          "Authorization code request failed with error: " + request.getError());
    }
    if (request.getCode() == null) {
      throw new OAuthCodeRequestFailException("Code or error parameter is required.");
    }
    AuthTokens authTokens = authService.loginWithGoogle(request.getCode());
    return new OAuthLoginResponse(authTokens);
  }

  @PostMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  public TokenRefreshResponse refreshAccessToken(
      @Valid @RequestBody TokenRefreshRequest request) {
    String accessToken = authService.refreshAccessToken(request.getRefresh());
    return new TokenRefreshResponse(accessToken);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public void logout(@Valid @RequestBody AuthLogoutRequest request) {
    authService.logout(request.getRefresh());
  }
}
