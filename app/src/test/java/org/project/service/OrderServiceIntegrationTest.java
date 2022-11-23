package org.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.configuration.OrderProperties;
import org.project.domain.Concert;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.project.dto.PreoccupyResult;
import org.project.exception.TicketNotFoundException;
import org.project.repository.ConcertRepository;
import org.project.repository.MemberRepository;
import org.project.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties({OrderProperties.class})
@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceIntegrationTest {

  @TestConfiguration
  static class OrderServiceTestContextConfiguration {

    static Clock defaultClock = Clock.fixed(Instant.parse("2020-01-01T00:00:00.00Z"),
        ZoneId.of("UTC"));

    @Bean
    @Primary
    public Clock clock() {
      return defaultClock;
    }
  }

  @Autowired
  private Clock clock;
  @Autowired
  private OrderProperties orderProperties;
  @Autowired
  private MemberRepository memberRepository;
  @SpyBean
  private ConcertRepository concertRepository;
  @SpyBean
  private TicketRepository ticketRepository;
  @Autowired
  private OrderService orderService;

  @AfterEach
  public void tearDown() {
    ticketRepository.deleteAll();
    memberRepository.deleteAll();
    concertRepository.deleteAll();
    orderService._setClock(OrderServiceTestContextConfiguration.defaultClock);
  }

  @Test
  @DisplayName("jpa 레포지토리 주입 테스트")
  public void injectedComponentsAreNotNull() {
    assertThat(memberRepository).isNotNull();
    assertThat(concertRepository).isNotNull();
    assertThat(ticketRepository).isNotNull();
  }

  @Test
  @DisplayName("선점 성공시 lockedUntil, lockedBy 값이 정상적으로 변경되는지 확인")
  public void preoccupySuccess() {
    // Setup
    Member member = new Member();
    member.setEmail("testemail@test.com");
    member.setProvider("google");
    memberRepository.save(member);
    Concert concert = new Concert("title", "description");
    concertRepository.save(concert);
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());

    // When
    PreoccupyResult preoccupyResult = orderService.preoccupy(member, ticketIds);

    // Then
    assertThat(preoccupyResult.getTicketIds()).isEqualTo(ticketIds);
    assertThat(preoccupyResult.getValidUntil()).isEqualTo(
        LocalDateTime.now(clock).plusSeconds(orderProperties.getPreoccupyExpireTime()));
    List<Ticket> ticketsAfter = ticketRepository.findAllById(ticketIds);
    ticketsAfter.forEach(ticket -> {
      assertThat(ticket.getLockedBy().getId()).isEqualTo(member.getId());
      assertThat(ticket.getLockedUntil()).isEqualTo(
          LocalDateTime.now(clock).plusSeconds(orderProperties.getPreoccupyExpireTime()));
    });
    assertThat(ticketsAfter.size()).isEqualTo(tickets.size());

  }

  @Test
  @DisplayName("선점 성공 이후에 같은 좌석을 다른 사용자가 선점하려고 할 때 실패하는지 확인")
  public void preoccupyFail() {
    // Setup
    Member member = new Member();
    member.setEmail("test@test.com");
    member.setProvider("google");
    memberRepository.save(member);
    Concert concert = new Concert("title", "description");
    concertRepository.save(concert);
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());

    // When
    // Then
    PreoccupyResult preoccupyResult = orderService.preoccupy(member, ticketIds);
    assertThat(preoccupyResult.getTicketIds()).isEqualTo(ticketIds);
    assertThat(preoccupyResult.getValidUntil()).isEqualTo(
        LocalDateTime.now(clock).plusSeconds(orderProperties.getPreoccupyExpireTime()));
    Member otherMember = new Member();
    otherMember.setEmail("other@member.com");
    otherMember.setProvider("google");
    memberRepository.save(otherMember);
    assertThatThrownBy(() -> orderService.preoccupy(otherMember, ticketIds))
        .isInstanceOf(TicketNotFoundException.class);
    List<Ticket> ticketsAfter = ticketRepository.findAllById(ticketIds);
    ticketsAfter.forEach(ticket -> {
      assertThat(ticket.getLockedBy().getId()).isEqualTo(member.getId());
      assertThat(ticket.getLockedUntil()).isEqualTo(
          LocalDateTime.now(clock).plusSeconds(orderProperties.getPreoccupyExpireTime()));
    });

  }

  @Test
  @DisplayName("선점 성공 이후 일정 시간 지난 후에는 다시 선점 가능한지 확인")
  public void preoccupySuccessAfterExpire() {
    // Setup
    Member member = new Member();
    member.setEmail("test@test.com");
    member.setProvider("google");
    memberRepository.save(member);
    Concert concert = new Concert("title", "description");
    concertRepository.save(concert);
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());

    // When
    PreoccupyResult preoccupyResult = orderService.preoccupy(member, ticketIds);
    Clock clockAfterTime = Clock.offset(clock,
        Duration.ofSeconds(orderProperties.getPreoccupyExpireTime() + 1));
    orderService._setClock(clockAfterTime);
    Member otherMember = new Member();
    otherMember.setEmail("other@other.com");
    otherMember.setProvider("google");
    memberRepository.save(otherMember);
    PreoccupyResult preoccupyResult2 = orderService.preoccupy(otherMember, ticketIds);

    // Then
    assertThat(preoccupyResult2.getTicketIds()).isEqualTo(ticketIds);
    assertThat(preoccupyResult2.getValidUntil()).isEqualTo(
        LocalDateTime.now(clockAfterTime).plusSeconds(orderProperties.getPreoccupyExpireTime()));
    List<Ticket> ticketsAfter = ticketRepository.findAllById(ticketIds);
    ticketsAfter.forEach(ticket -> {
      assertThat(ticket.getLockedBy().getId()).isEqualTo(otherMember.getId());
      assertThat(ticket.getLockedUntil()).isEqualTo(
          LocalDateTime.now(clockAfterTime).plusSeconds(orderProperties.getPreoccupyExpireTime()));
    });
    assertThat(ticketsAfter.size()).isEqualTo(tickets.size());


  }

}
