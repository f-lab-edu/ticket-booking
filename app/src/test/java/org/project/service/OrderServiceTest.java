package org.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.configuration.OrderProperties;
import org.project.domain.Concert;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.project.dto.PreoccupyResult;
import org.project.exception.ConcertNotFoundException;
import org.project.exception.TicketNotFoundException;
import org.project.repository.ConcertRepository;
import org.project.repository.PreoccupyRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneId.of("UTC"));
  private final ConcertRepository concertRepository = mock(ConcertRepository.class);
  private final PreoccupyRepository preoccupyRepository = mock(PreoccupyRepository.class);
  private final OrderProperties orderProperties = new OrderProperties(10L);
  private final OrderService orderService = new OrderService(clock, concertRepository,
      orderProperties, preoccupyRepository);

  @Test
  @DisplayName("존재하는 concertId와 유효한 member로 선점을 요청한다.")
  public void test_preoccupy() {
    // given
    Long concertId = 1L;
    Concert mockConcert = mock(Concert.class);
    Member mockMember = mock(Member.class);
    Ticket mockTicket = mock(Ticket.class);
    given(concertRepository.findById(concertId)).willReturn(Optional.of(mockConcert));
    given(mockConcert.getAvailableTickets(1)).willReturn(new ArrayList<>(List.of(mockTicket)));
    given(preoccupyRepository.isPreoccupied(mockTicket.getId())).willReturn(false);
    LocalDateTime validUntil = LocalDateTime.now(clock)
        .plusSeconds(orderProperties.getPreoccupyExpireTime());

    // when
    PreoccupyResult preoccupyResult = orderService.preoccupy(concertId, mockMember);

    // then
    assertThat(preoccupyResult.getTicketId()).isEqualTo(mockTicket.getId());
    assertThat(preoccupyResult.getValidUntil()).isEqualTo(validUntil);
    then(concertRepository).should().findById(concertId);
    then(mockConcert).should().getAvailableTickets(1);
    then(preoccupyRepository).should()
        .save(mockTicket, mockMember, orderProperties.getPreoccupyExpireTime());
  }

  @Test
  @DisplayName("존재하지 않는 concertId로 선점을 요청하면 ConcertNotFoundException을 던진다.")
  public void test_preoccupy_with_not_found_concert() {
    // given
    Long concertId = 1L;
    given(concertRepository.findById(concertId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> orderService.preoccupy(concertId, mock(Member.class)))
        .isInstanceOf(ConcertNotFoundException.class);
    then(concertRepository).should().findById(concertId);
    then(preoccupyRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("concert는 존재하지만 티켓이 없는 경우 TicketNotFoundException을 던진다.")
  public void test_no_available_ticket_case() {
    // given
    Long concertId = 1L;
    Concert mockConcert = mock(Concert.class);
    given(concertRepository.findById(concertId)).willReturn(Optional.of(mockConcert));
    TicketNotFoundException ticketNotFoundException = new TicketNotFoundException(concertId);
    given(mockConcert.getAvailableTickets(1)).willThrow(ticketNotFoundException);

    // when
    // then
    assertThatThrownBy(() -> orderService.preoccupy(concertId, mock(Member.class)))
        .isInstanceOf(TicketNotFoundException.class);
    then(concertRepository).should().findById(concertId);
    then(mockConcert).should().getAvailableTickets(1);
    then(preoccupyRepository).shouldHaveNoInteractions();
  }
}
