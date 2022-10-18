package org.project.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GoogleUserInfoResponse {

  @JsonProperty("email")
  private String email;
  @JsonProperty("family_name")
  private String familyName;
  @JsonProperty("gender")
  private String gender;
  @JsonProperty("given_name")
  private String givenName;
  @JsonProperty("hd")
  private String hd;
  @JsonProperty("id")
  private String id;
  @JsonProperty("link")
  private String link;
  @JsonProperty("locale")
  private String locale;
  @JsonProperty("name")
  private String name;
  @JsonProperty("picture")
  private String picture;
  @JsonProperty("verified_email")
  private boolean verifiedEmail;
}
