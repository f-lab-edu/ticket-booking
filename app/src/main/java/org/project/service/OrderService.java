package org.project.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.NotImplementedException;
import org.project.configuration.OrderProperties;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.project.domain.TicketOrder;
import org.project.dto.PreoccupyResult;
import org.project.exception.TicketNotFoundException;
import org.project.repository.ConcertRepository;
import org.project.repository.PreoccupyRepository;
import org.project.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final Clock clock;
  private final ConcertRepository concertRepository;
  private final OrderProperties orderProperties;
  private final PreoccupyRepository preoccupyRepository;
  private final TicketRepository ticketRepository;

  public OrderService(Clock clock, ConcertRepository concertRepository,
      OrderProperties orderProperties,
      PreoccupyRepository preoccupyRepository,
      TicketRepository ticketRepository) {
    this.clock = clock;
    this.concertRepository = concertRepository;
    this.orderProperties = orderProperties;
    this.preoccupyRepository = preoccupyRepository;
    this.ticketRepository = ticketRepository;
  }

  public TicketOrder book(Long concertId, Member member) {

    throw new NotImplementedException();
  }

  @Transactional
  public PreoccupyResult preoccupy(Member member, List<Long> ticketIds) {

    // check if tickets exist
    List<Ticket> tickets = ticketRepository.findAllByIdAndLockedUntilAfterWithLock(ticketIds,
        LocalDateTime.now(clock));
    if (tickets.size() != ticketIds.size()) {
      throw new TicketNotFoundException(ticketIds);
    }

    // set tickets lockedUntil
    LocalDateTime validUntil = LocalDateTime.now(clock)
        .plusSeconds(orderProperties.getPreoccupyExpireTime());
    int lockedTicketCounts = ticketRepository.lockTickets(
        member.getId(),
        tickets.stream().map(Ticket::getId).collect(Collectors.toList()),
        validUntil);
    if (lockedTicketCounts != ticketIds.size()) {
      throw new TicketNotFoundException(ticketIds);
    }

    return new PreoccupyResult(ticketIds, validUntil);
  }

}
