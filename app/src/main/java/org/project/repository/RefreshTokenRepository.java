package org.project.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.project.configuration.JwtProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

  private final RedisTemplate<String, Object> redisTemplate;
  private final JwtProperties jwtProperties;

  public RefreshTokenRepository(
      RedisTemplate<String, Object> redisTemplate,
      JwtProperties jwtProperties
  ) {
    this.redisTemplate = redisTemplate;
    this.jwtProperties = jwtProperties;
  }

  public void save(String refreshToken) {
    Long refreshExpireTimeInSeconds = this.jwtProperties.getRefreshExpiration();
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
