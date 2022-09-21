package org.project.domain.member.controller;

import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final OAuthService oAuthService;

  public OAuthController(OAuthService oAuthService) {
    this.oAuthService = oAuthService;
  }

  @GetMapping("/google")
  public ResponseEntity<AuthTokens> loginWithGoogle(@RequestParam(required = false) String code,
      @RequestParam(required = false) String error) {

    return ResponseEntity.ok(oAuthService.loginWithGoogle(code));
  }

  @GetMapping("/kakao")
  public ResponseEntity<AuthTokens> loginWithKakao(@RequestParam(required = false) String code,
      @RequestParam(required = false) String error) {

    return ResponseEntity.ok(oAuthService.loginWithKakao(code));
  }

}
