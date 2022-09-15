package org.project.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


/**
 * Authorization server에 access 토큰 요청 포맷. RFC 6749 내용 준수. (<a
 * href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.4.2">링크</a>)
 */
@Getter
@ToString
@AllArgsConstructor
public class OAuthAccessTokenRequest {

  private String code;
  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("client_secret")
  private String clientSecret;
  @JsonProperty("redirect_uri")
  private String redirectUri;
  @JsonProperty("grant_type")
  private String grantType;

}
