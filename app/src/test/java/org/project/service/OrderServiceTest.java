package org.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.Invocation;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.configuration.OrderProperties;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.project.dto.PreoccupyResult;
import org.project.exception.TicketNotFoundException;
import org.project.repository.ConcertRepository;
import org.project.repository.PreoccupyRepository;
import org.project.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T00:00:00Z"), ZoneId.of("UTC"));
  private final ConcertRepository concertRepository = mock(ConcertRepository.class);
  private final PreoccupyRepository preoccupyRepository = mock(PreoccupyRepository.class);
  private final OrderProperties orderProperties = new OrderProperties(10L);
  private final TicketRepository ticketRepository = mock(TicketRepository.class);
  private final OrderService orderService = new OrderService(clock, concertRepository,
      orderProperties, preoccupyRepository, ticketRepository);

  @Test
  @DisplayName("요청받은 티켓 목록이 선점되어 있지 않으면 정상 응답한다.")
  public void givenNonReservedTicketsMethodWillReturnSuccess() {
    // given
    // Mocking member entity
    Long memberId = 1L;
    Member mockMember = mock(Member.class);
    given(mockMember.getId()).willReturn(memberId);
    // Mocking three ticket entities
    List<Long> ticketIds = List.of(1L, 2L, 3L);
    List<Ticket> tickets = List.of(mock(Ticket.class), mock(Ticket.class), mock(Ticket.class));
    for (int i = 0; i < ticketIds.size(); i++) {
      given(tickets.get(i).getId()).willReturn(ticketIds.get(i));
    }
    given(ticketRepository.findAllByIdAndLockedUntilAfterWithLock(eq(ticketIds),
        eq(LocalDateTime.now(clock))))
        .willReturn(tickets);
    LocalDateTime validUntil = LocalDateTime.now(clock)
        .plusSeconds(orderProperties.getPreoccupyExpireTime());
    given(ticketRepository.lockTickets(eq(mockMember.getId()), eq(ticketIds), eq(validUntil)))
        .willReturn(tickets.size());

    // when
    PreoccupyResult preoccupyResult = orderService.preoccupy(mockMember, ticketIds);
    Collection<Invocation> invocations = mockingDetails(ticketRepository).getInvocations();

    // then
    assertThat(preoccupyResult.getTicketIds()).isEqualTo(ticketIds);
    assertThat(preoccupyResult.getValidUntil()).isEqualTo(validUntil);
    then(ticketRepository).should()
        .findAllByIdAndLockedUntilAfterWithLock(eq(ticketIds), eq(LocalDateTime.now(clock)));
    then(ticketRepository).should()
        .lockTickets(eq(mockMember.getId()), eq(ticketIds), eq(validUntil));

  }

  @Test
  @DisplayName("요청받은 티켓 목록 중 하나라도 선점되어 있거나 존재하지 않으면 TicketNofFoundException을 던진다.")
  public void testWhenSomeTicketsAreAlreadyReserved() {
    // given
    // Mocking member entity
    Long memberId = 1L;
    Member mockMember = mock(Member.class);
    // Mocking three ticket entities
    List<Long> ticketIds = List.of(1L, 2L, 3L);
    List<Ticket> tickets = List.of(mock(Ticket.class), mock(Ticket.class), mock(Ticket.class));
    for (int i = 0; i < ticketIds.size(); i++) {
      given(tickets.get(i).getId()).willReturn(ticketIds.get(i));
    }

    List<Ticket> ticketsWithMissing = tickets.stream().filter(t -> t.getId() != 3L)
        .collect(Collectors.toList());
    given(ticketRepository.findAllByIdAndLockedUntilAfterWithLock(eq(ticketIds),
        eq(LocalDateTime.now(clock))))
        .willReturn(ticketsWithMissing);

    // when
    // then
    assertThatThrownBy(() -> orderService.preoccupy(mockMember, ticketIds))
        .isInstanceOf(TicketNotFoundException.class);
    then(ticketRepository).should()
        .findAllByIdAndLockedUntilAfterWithLock(eq(ticketIds), eq(LocalDateTime.now(clock)));
    then(ticketRepository).shouldHaveNoMoreInteractions();

  }

  @Test
  @DisplayName("요청받은 티켓 목록을 선점 시도하였으나 실패한 티켓이 있는 경우 TicketNofFoundException을 던진다.")
  public void testWhenSomeTicketReservationFailed() {
    // given
    // Mocking member entity
    Long memberId = 1L;
    Member mockMember = mock(Member.class);
    given(mockMember.getId()).willReturn(memberId);
    // Mocking three ticket entities
    List<Long> ticketIds = List.of(1L, 2L, 3L);
    List<Ticket> tickets = List.of(mock(Ticket.class), mock(Ticket.class), mock(Ticket.class));
    for (int i = 0; i < ticketIds.size(); i++) {
      given(tickets.get(i).getId()).willReturn(ticketIds.get(i));
    }
    given(ticketRepository.findAllByIdAndLockedUntilAfterWithLock(eq(ticketIds),
        eq(LocalDateTime.now(clock))))
        .willReturn(tickets);
    LocalDateTime validUntil = LocalDateTime.now(clock)
        .plusSeconds(orderProperties.getPreoccupyExpireTime());
    given(ticketRepository.lockTickets(eq(mockMember.getId()), eq(ticketIds), eq(validUntil)))
        .willReturn(tickets.size() - 1);

    // when
    // then
    assertThatThrownBy(() -> orderService.preoccupy(mockMember, ticketIds))
        .isInstanceOf(TicketNotFoundException.class);
    then(ticketRepository).should()
        .findAllByIdAndLockedUntilAfterWithLock(eq(ticketIds), eq(LocalDateTime.now(clock)));
    then(ticketRepository).should()
        .lockTickets(eq(mockMember.getId()), eq(ticketIds), eq(validUntil));

  }

}
