package org.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PreoccupyResponse {

  @JsonProperty("ticket_id")
  private Long ticketId;

  @JsonProperty("valid_until")
  private LocalDateTime validUntil;

}
