package org.project.service;

import org.apache.commons.lang3.NotImplementedException;
import org.project.configuration.OrderProperties;
import org.project.domain.Concert;
import org.project.domain.Member;
import org.project.domain.TicketOrder;
import org.project.dto.PreoccupyResult;
import org.project.exception.ConcertNotFoundException;
import org.project.repository.ConcertRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final ConcertRepository concertRepository;
  private final OrderProperties orderProperties;

  public OrderService(ConcertRepository concertRepository, OrderProperties orderProperties) {
    this.concertRepository = concertRepository;
    this.orderProperties = orderProperties;
  }

  public TicketOrder book(Long concertId, Member member) {

    throw new NotImplementedException();
  }

  public PreoccupyResult preoccupy(Long concertId, Member member) {

    // find concert
    Concert concert = concertRepository.findById(concertId)
        .orElseThrow(() -> new ConcertNotFoundException(concertId));

    throw new NotImplementedException("Preoccupy not implemented");
  }

}
