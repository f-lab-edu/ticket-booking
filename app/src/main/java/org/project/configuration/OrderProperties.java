package org.project.configuration;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties("order")
public class OrderProperties {

  @NotNull
  private final Long preoccupyExpireTime;

}
