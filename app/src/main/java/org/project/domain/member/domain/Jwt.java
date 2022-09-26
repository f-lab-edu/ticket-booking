package org.project.domain.member.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Jwt {

  String token;
  LocalDateTime exp;
  String sub;

  boolean isExpired() {
    throw new UnsupportedOperationException("jwt.isExpired() not implemented yet.");
  }

  @Override
  public String toString() {
    if (token == null) {
      return "";
    }
    return token;
  }
}
