package org.project.domain.user.dto;

import lombok.Getter;
import org.project.domain.user.domain.Jwt;

@Getter
public class AuthTokens {

  private final Jwt access;
  private final Jwt refresh;

  public AuthTokens(Jwt accessToken, Jwt refreshToken) {
    this.access = accessToken;
    this.refresh = refreshToken;
  }
}
