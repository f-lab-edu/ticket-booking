package org.project.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.project.configuration.OrderProperties;
import org.project.domain.Concert;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.project.domain.TicketOrder;
import org.project.dto.PreoccupyResult;
import org.project.exception.ConcertNotFoundException;
import org.project.exception.TicketNotFoundException;
import org.project.repository.ConcertRepository;
import org.project.repository.PreoccupyRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final Clock clock;
  private final ConcertRepository concertRepository;
  private final OrderProperties orderProperties;
  private final PreoccupyRepository preoccupyRepository;

  public OrderService(Clock clock, ConcertRepository concertRepository,
      OrderProperties orderProperties,
      PreoccupyRepository preoccupyRepository) {
    this.clock = clock;
    this.concertRepository = concertRepository;
    this.orderProperties = orderProperties;
    this.preoccupyRepository = preoccupyRepository;
  }

  public TicketOrder book(Long concertId, Member member) {

    throw new NotImplementedException();
  }

  public PreoccupyResult preoccupy(Long concertId, Member member) {

    // find concert
    Concert concert = concertRepository.findById(concertId)
        .orElseThrow(() -> new ConcertNotFoundException(concertId));

    // get a ticket
    List<Ticket> availableTickets = concert.getAvailableTickets(1);

    // find which is not preoccupied (처음 구현시 생각 못 한 부분)
    Ticket ticket = availableTickets.stream()
        .filter(t -> !preoccupyRepository.isPreoccupied(t.getId()))
        .findFirst()
        .orElseThrow(() -> new TicketNotFoundException(concert));

    // preoccupy ticket
    preoccupyRepository.save(ticket, member, orderProperties.getPreoccupyExpireTime());
    LocalDateTime validUntil = LocalDateTime.now(clock)
        .plusSeconds(orderProperties.getPreoccupyExpireTime());

    return new PreoccupyResult(ticket.getId(), validUntil);
  }

}
