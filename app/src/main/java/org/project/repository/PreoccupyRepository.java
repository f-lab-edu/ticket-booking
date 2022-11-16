package org.project.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.project.domain.Member;
import org.project.domain.Ticket;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PreoccupyRepository {

  private final RedisTemplate<String, Long> redisTemplate;

  public PreoccupyRepository(RedisTemplate<String, Long> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Transactional
  public void save(Ticket ticket, Member member, Long preoccupyTime) {
    redisTemplate.opsForValue().set(ticket.getId().toString(), member.getId());
    redisTemplate.expire(ticket.getId().toString(), preoccupyTime, TimeUnit.SECONDS);
  }

  public Optional<Long> findMemberId(Ticket ticket) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(ticket.getId().toString()));
  }

  public Boolean isPreoccupied(Long ticketId) {
    return redisTemplate.hasKey(ticketId.toString());
  }
}
