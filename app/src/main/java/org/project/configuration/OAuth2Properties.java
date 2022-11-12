package org.project.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties("oauth2")
public class OAuth2Properties {

  @Valid
  private final Google google;

  public OAuth2Properties(Google google) {
    this.google = google;
  }

  @Getter
  public static class Google {

    @NotNull
    private final String clientId;
    @NotNull
    private final String clientSecret;
    @NotNull
    private final String redirectUri;

    public Google(String clientId, String clientSecret, String redirectUri) {
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.redirectUri = redirectUri;
    }
  }
}
