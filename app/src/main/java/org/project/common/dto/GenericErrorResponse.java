package org.project.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericErrorResponse {

  private final String timestamp;
  private final int status;
  private final String error;
  private final String message;
}
