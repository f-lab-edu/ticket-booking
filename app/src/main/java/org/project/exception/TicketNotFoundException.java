package org.project.exception;

import org.project.domain.Concert;

public class TicketNotFoundException extends NotFoundException {

  public TicketNotFoundException() {
    super("Ticket not found");
  }

  public TicketNotFoundException(Long id) {
    super("Ticket not found for given ticket id: " + id);
  }

  public TicketNotFoundException(Concert concert) {
    super("Ticket not found for given concert id: " + concert.getId());
  }

}
