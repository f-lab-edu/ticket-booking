package org.project.repository;

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
        .set(refreshToken, "", refreshExpireTimeInSeconds, TimeUnit.SECONDS); // value 불필요
  }

  public Boolean exists(String refreshToken) {
    return redisTemplate.hasKey(refreshToken);
  }

  public Boolean delete(String refreshToken) {
    return redisTemplate.delete(refreshToken);
  }
}
