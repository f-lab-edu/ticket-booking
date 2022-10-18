package org.project.domain.user.controller;

import javax.validation.Valid;
import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.dto.OAuthLoginQueryParameter;
import org.project.domain.user.dto.OAuthLoginResponse;
import org.project.domain.user.dto.AuthLogoutRequest;
import org.project.domain.user.service.AuthService;
import org.project.domain.user.dto.TokenRefreshRequest;
import org.project.domain.user.dto.TokenRefreshResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/google")
  public ResponseEntity<OAuthLoginResponse> loginWithGoogle(OAuthLoginQueryParameter request) {
    if (request.getError() != null) {
      // TODO: 어떤 Error 던질 것인지
      throw new UnsupportedOperationException(
          "oAuthLoginController.loginWithGoogle() error handling not implemented.");
    }
    AuthTokens authTokens = authService.loginWithGoogle(request.getCode());
    return ResponseEntity.ok(new OAuthLoginResponse(authTokens));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenRefreshResponse> refreshAccessToken(
      @Valid @RequestBody TokenRefreshRequest request) {
    String accessToken = authService.refreshAccessToken(request.getRefresh());
    return ResponseEntity.ok(new TokenRefreshResponse(accessToken));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Valid @RequestBody AuthLogoutRequest request) {
    authService.logout(request.getRefresh());
    return ResponseEntity.ok().build();
  }
}
