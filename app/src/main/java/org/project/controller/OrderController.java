package org.project.controller;

import javax.validation.Valid;
import org.project.annotation.AccessTokenRequired;
import org.project.domain.Member;
import org.project.domain.TicketOrder;
import org.project.dto.OrderRequest;
import org.project.dto.OrderResponse;
import org.project.dto.PreoccupyResponse;
import org.project.dto.PreoccupyResult;
import org.project.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  @AccessTokenRequired
  @ResponseStatus(HttpStatus.OK)
  public OrderResponse order(Member member, @Valid @RequestBody OrderRequest request) {

    TicketOrder order = orderService.book(request.getConcertId(), member);
    return new OrderResponse(order);
  }

  @PostMapping("/preoccupy")
  @AccessTokenRequired
  @ResponseStatus(HttpStatus.OK)
  public PreoccupyResponse preoccupy(Member member, @Valid @RequestBody OrderRequest request) {

    PreoccupyResult preoccupyResult = orderService.preoccupy(request.getConcertId(), member);
    return new PreoccupyResponse(preoccupyResult.getTicketId(), preoccupyResult.getValidUntil());
  }
}
