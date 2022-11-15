package org.project.service;

import org.apache.commons.lang3.NotImplementedException;
import org.project.domain.Member;
import org.project.domain.TicketOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  public TicketOrder book(Long concertId, Member member) {

    throw new NotImplementedException();
  }

}
