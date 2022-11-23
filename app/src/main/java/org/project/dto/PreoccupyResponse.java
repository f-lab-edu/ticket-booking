package org.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PreoccupyResponse {

  @JsonProperty("ticket_ids")
  private List<Long> ticketIds;

  @JsonProperty("valid_until")
  private LocalDateTime validUntil;

}
