package org.project.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreoccupyResult {

  Long ticketId;

  LocalDateTime validUntil;
}
