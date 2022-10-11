package org.project.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoogleUserInfoErrorResponse {

  private Integer code;
  private String message;
  private String status;

  @JsonProperty("error")
  private void unpackNested(Map<String, Object> error) {
    this.code = (Integer) error.get("code");
    this.message = (String) error.get("message");
    this.status = (String) error.get("status");
  }

}
