package org.project.domain.user.controller;

import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.dto.OAuthLoginQueryParameter;
import org.project.domain.user.dto.OAuthLoginResponse;
import org.project.domain.user.service.OAuthLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthLoginController {

  private final OAuthLoginService oAuthLoginService;

  public OAuthLoginController(OAuthLoginService oAuthLoginService) {
    this.oAuthLoginService = oAuthLoginService;
  }

  @GetMapping("/google")
  public ResponseEntity<OAuthLoginResponse> loginWithGoogle(OAuthLoginQueryParameter request) {
    if (request.getError() != null) {
      // TODO: 어떤 Error 던질 것인지
      throw new UnsupportedOperationException(
          "oAuthLoginController.loginWithGoogle() error handling not implemented.");
    }
    AuthTokens authTokens = oAuthLoginService.loginWithGoogle(request.getCode());
    return ResponseEntity.ok(new OAuthLoginResponse(authTokens));
  }
}
