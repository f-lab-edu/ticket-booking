package org.project.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoogleAccessTokenResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("expires_in")
  private int expiresIn;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("scope")
  private String scope;
}
