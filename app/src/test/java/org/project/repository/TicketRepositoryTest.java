package org.project.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.domain.Concert;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TicketRepositoryTest {

  @Autowired
  private TicketRepository ticketRepository;
  @Autowired
  private ConcertRepository concertRepository;
  @Autowired
  private MemberRepository memberRepository;

  @TestConfiguration
  static class TicketRepositoryTestContextConfiguration {

    static Clock clock = Clock.fixed(Instant.parse("2020-01-01T00:00:00.00Z"), ZoneId.of("UTC"));

    @Bean
    public Clock clock() {
      return clock;
    }
  }

  @Test
  @DisplayName("findAllByIdAndLockedUntilAfterWithLock() 테스트 with null lockedUntil")
  public void testFindAllByIdAndLockedUntilAfterWithLock() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    memberRepository.saveAndFlush(member);
    Concert concert = new Concert("title", "desc.");
    concertRepository.saveAndFlush(concert);
    ArrayList<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());
    LocalDateTime now = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock);

    // When
    List<Ticket> result = ticketRepository.findAllByIdAndLockedUntilAfterWithLock(ticketIds, now);

    // Then
    assertThat(result).hasSize(10);
  }

  @Test
  @DisplayName("findAllByIdAndLockedUntilAfterWithLock() 테스트 with lockedUntil이 now() 이전인 케이스")
  public void testFindAllByIdAndLockedUntilAfterWithLock2() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    memberRepository.saveAndFlush(member);
    Concert concert = new Concert("title", "desc.");
    concertRepository.saveAndFlush(concert);
    ArrayList<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      ticket.setLockedUntil(LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock)
          .minusSeconds(1));
      ticket.setLockedBy(member);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());
    LocalDateTime now = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock);

    // When
    List<Ticket> result = ticketRepository.findAllByIdAndLockedUntilAfterWithLock(ticketIds, now);

    // Then
    assertThat(result).hasSize(10);
  }

  @Test
  @DisplayName("findAllByIdAndLockedUntilAfterWithLock() 테스트 with lockedUntil이 now() 이후인 케이스")
  public void testFindAllByIdAndLockedUntilAfterWithLock3() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    memberRepository.saveAndFlush(member);
    Concert concert = new Concert("title", "desc.");
    concertRepository.saveAndFlush(concert);
    ArrayList<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      ticket.setLockedUntil(LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock)
          .plusSeconds(1));
      ticket.setLockedBy(member);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());
    LocalDateTime now = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock);

    // When
    List<Ticket> result = ticketRepository.findAllByIdAndLockedUntilAfterWithLock(ticketIds, now);

    // Then
    assertThat(result).hasSize(0);

  }

  @Test
  @DisplayName("findAllByIdAndLockedUntilAfterWithLock() 테스트 with 존재하지 않는 티켓")
  public void testFindAllByIdAndLockedUntilAfterWithLock4() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    memberRepository.saveAndFlush(member);
    Concert concert = new Concert("title", "desc.");
    concertRepository.saveAndFlush(concert);
    ArrayList<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAllAndFlush(tickets);

    // Given
    List<Long> ticketIds = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      ticketIds.add(100L + i);
    }
    LocalDateTime now = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock);

    // When
    List<Ticket> result = ticketRepository.findAllByIdAndLockedUntilAfterWithLock(ticketIds, now);

    // Then
    assertThat(result).hasSize(0);
  }

  @Test
  @DisplayName("lockTickets() 테스트 with 성공 케이스")
  public void testLockTickets() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    memberRepository.save(member);
    Concert concert = new Concert("title", "desc.");
    concertRepository.save(concert);
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAll(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());
    LocalDateTime lockUntil = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock)
        .plusSeconds(10);

    // When
    int result = ticketRepository.lockTickets(member, ticketIds, lockUntil);
    ticketRepository.flush();

    // Then
    assertThat(result).isEqualTo(10);
    List<Ticket> attachedTickets = ticketRepository.findAllById(ticketIds);
    Long memberId = member.getId();
    attachedTickets.forEach(ticket -> {
      assertThat(ticket.getLockedUntil()).isEqualTo(lockUntil);
      assertThat(ticket.getLockedBy().getId()).isEqualTo(memberId);
    });
  }

  @Test
  @DisplayName("lockTickets() 테스트 with 존재하지 않는 티켓")
  public void testLockTickets2() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    memberRepository.save(member);
    Concert concert = new Concert("title", "desc.");
    concertRepository.save(concert);
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAll(tickets);

    // Given
    List<Long> ticketIds = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      ticketIds.add(100L + i);
    }
    LocalDateTime lockUntil = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock)
        .plusSeconds(10);

    // When
    int result = ticketRepository.lockTickets(member, ticketIds, lockUntil);
    ticketRepository.flush();

    // Then
    assertThat(result).isEqualTo(0);

  }

  @Test
  @DisplayName("lockTickets() 테스트 with 영속화되지 않은 Member throws InvalidDataAccessApiUsageException")
  public void testLockTickets3() {

    // Set up
    Member member = new Member();
    member.setProvider("google");
    member.setEmail("test@test.com");
    Concert concert = new Concert("title", "desc.");
    concertRepository.save(concert);
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Ticket ticket = new Ticket();
      ticket.setConcert(concert);
      tickets.add(ticket);
    }
    ticketRepository.saveAll(tickets);

    // Given
    List<Long> ticketIds = tickets.stream().map(Ticket::getId)
        .collect(Collectors.toList());
    LocalDateTime lockUntil = LocalDateTime.now(TicketRepositoryTestContextConfiguration.clock)

        .plusSeconds(10);

    // When
    // then
    assertThatThrownBy(() -> ticketRepository.lockTickets(member, ticketIds, lockUntil))
        .isInstanceOf(InvalidDataAccessApiUsageException.class);

  }


}
