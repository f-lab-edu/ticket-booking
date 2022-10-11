package org.project.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GoogleAccessTokenRequest {

  @JsonProperty("code")
  private String code;
  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("client_secret")
  private String clientSecret;
  @JsonProperty("redirect_uri")
  private String redirectUri;
  @JsonProperty("grant_type")
  private String grantType = "authorization_code";
}
