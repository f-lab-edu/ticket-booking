package org.project.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreoccupyResult {

  List<Long> ticketIds;

  LocalDateTime validUntil;
}
