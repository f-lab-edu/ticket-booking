package org.project.domain.member.controller;

import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

  @Value("${oauth2.google.client-id}")
  private String googleClientId;
  @Value("${oauth2.google.client-secret}")
  private String googleClientSecret;

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final OAuthService oAuthService;

  public OAuthController(OAuthService oAuthService) {
    this.oAuthService = oAuthService;
  }

  @RequestMapping("/google")
  public ResponseEntity<AuthTokens> loginWithGoogle(@RequestParam String code) {
    return ResponseEntity.ok(oAuthService.loginWithGoogle(code));
  }
}
