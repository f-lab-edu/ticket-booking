package org.project.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.domain.Member;
import org.project.domain.TicketOrder;
import org.project.dto.OrderRequest;
import org.project.dto.OrderResponse;
import org.project.service.OrderService;

public class OrderControllerTest {

  private final OrderService orderService = mock(OrderService.class);
  private final OrderController orderController = new OrderController(orderService);

  @Test
  @DisplayName("order()는 넘겨받은 member와 concert id를 통해 orderService.book()을 호출한다.")
  void testBook() {
    // given
    Long concertId = 1L;
    Member member = mock(Member.class);
    OrderRequest orderRequest = new OrderRequest(concertId);

    Long orderId = 1L;
    TicketOrder order = mock(TicketOrder.class);
    given(order.getId()).willReturn(orderId);
    given(orderService.book(anyLong(), any(Member.class))).willReturn(order);

    // when
    OrderResponse orderResponse = orderController.order(member, orderRequest);

    // then
    // orderService.book()이 올바른 인자로 호출되었는지 확인
    then(orderService).should().book(concertId, member);
    // orderResponse가 올바른 값을 가지고 있는지 확인
    assertThat(orderResponse.getOrderId()).isEqualTo(orderId);
  }
}
