package org.project.domain.member.dto;

import lombok.Getter;
import org.project.domain.member.domain.Jwt;

@Getter
public class AuthTokens {

  private final Jwt access;
  private final Jwt refresh;

  public AuthTokens(Jwt accessToken, Jwt refreshToken) {
    this.access = accessToken;
    this.refresh = refreshToken;
  }
}
