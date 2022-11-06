package org.project.dto;

import lombok.Getter;

@Getter
public class AuthTokens {

  private final String access;
  private final String refresh;

  public AuthTokens(String accessToken, String refreshToken) {
    this.access = accessToken;
    this.refresh = refreshToken;
  }
}
