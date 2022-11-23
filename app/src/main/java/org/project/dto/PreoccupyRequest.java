package org.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreoccupyRequest {

  @NotNull(message = "concert id is required")
  @JsonProperty("concert_id")
  private Long concertId;

  @NotEmpty(message = "ticket ids are required")
  @JsonProperty("ticket_ids")
  private List<Long> ticketIds;
}
