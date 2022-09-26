package org.project.domain.user.dto;

import lombok.Getter;

@Getter
public class OAuthLoginResponse {

  private final String access;
  private final String refresh;

  public OAuthLoginResponse(AuthTokens authTokens) {
    this.access = authTokens.getAccess().toString();
    this.refresh = authTokens.getRefresh().toString();
  }
}
