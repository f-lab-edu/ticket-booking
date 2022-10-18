package org.project.domain.user.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

  private final RedisTemplate<String, Object> redisTemplate;
  private final Long refreshExpireTimeInSeconds;

  public RefreshTokenRepository(
      RedisTemplate<String, Object> redisTemplate,
      @Value("${jwt.refresh-expiration}") Long refreshExpireTimeInSeconds) {
    this.redisTemplate = redisTemplate;
    this.refreshExpireTimeInSeconds = refreshExpireTimeInSeconds;
  }

  public void save(String refreshToken) {
    redisTemplate.opsForValue()
        .set(refreshToken, String.valueOf(refreshExpireTimeInSeconds));
    redisTemplate.expire(refreshToken, refreshExpireTimeInSeconds, TimeUnit.SECONDS);
  }

  public Optional<Long> find(String refreshToken) {
    String value = (String) redisTemplate.opsForValue().get(refreshToken);
    if (value == null) {
      return Optional.empty();
    }
    return Optional.of(Long.parseLong(value));
  }

  public Boolean delete(String refreshToken) {
    return redisTemplate.delete(refreshToken);
  }
}
