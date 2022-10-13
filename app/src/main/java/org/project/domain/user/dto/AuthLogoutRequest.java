package org.project.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthLogoutRequest {

  @NotNull(message = "refresh token is required")
  @JsonProperty("refresh")
  private String refresh;

}
