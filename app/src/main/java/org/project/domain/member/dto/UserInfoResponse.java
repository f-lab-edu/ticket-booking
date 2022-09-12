package org.project.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Shcema reference from <a
 * href="googleapis.com/discovery/v1/apis/oauth2/v2/rest">googleapis.com/discovery/v1/apis/oauth2/v2/rest</a>
 */
@Getter
@ToString
public class UserInfoResponse {

  /**
   * The user's email address.
   */
  private String email;

  /**
   * The user's last name.맷
   */
  @JsonProperty("family_name")
  private String familyName;

  /**
   * The user's gender.
   */
  private String gender;

  /**
   * The user's first name.
   */
  @JsonProperty("given_name")
  private String givenName;

  /**
   * The hosted domain e.g. example.com if the user is Google apps user.
   */
  private String hd;

  /**
   * The obfuscated ID of the user.
   */
  private String id;

  /**
   * URL of the profile page.
   */
  private String link;

  /**
   * The user's preferred locale.
   */
  private String locale;

  /**
   * The user's full name.
   */
  private String name;

  /**
   * URL of the user's picture image.
   */
  private String picture;

  /**
   * Boolean flag which is true if the email address is verified. Always verified because we only
   * return the user's primary email address.
   */
  @JsonProperty("verified_email")
  private Boolean verifiedEmail;
}
