package org.project.dto;

import lombok.Getter;
import org.project.domain.TicketOrder;

@Getter
public class OrderResponse {

  private final Long orderId;


  public OrderResponse(TicketOrder order) {
    this.orderId = order.getId();
  }
}
