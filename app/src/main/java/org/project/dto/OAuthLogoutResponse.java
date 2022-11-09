package org.project.dto;

import lombok.Getter;

@Getter
public class OAuthLogoutResponse {

  private final String message;

  public OAuthLogoutResponse(String message) {
    this.message = message;
  }

}
