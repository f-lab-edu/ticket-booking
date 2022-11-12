package org.project.configuration;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties("jwt")
public class JwtProperties {

  @NotNull
  private final String accessSecret;
  @NotNull
  private final String refreshSecret;
  @NotNull
  private final Long accessExpiration;
  @NotNull
  private final Long refreshExpiration;

  public JwtProperties(String accessSecret, String refreshSecret, Long accessExpiration,
      Long refreshExpiration) {
    this.accessSecret = accessSecret;
    this.refreshSecret = refreshSecret;
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;
  }
}
