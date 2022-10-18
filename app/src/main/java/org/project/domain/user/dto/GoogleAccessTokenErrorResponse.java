package org.project.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoogleAccessTokenErrorResponse {

  @JsonProperty("error")
  private String error;
  @JsonProperty("error_description")
  private String errorDescription;
}
