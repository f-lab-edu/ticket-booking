package org.project.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthLoginQueryParameter {

  private String code;
  private String error;

}
