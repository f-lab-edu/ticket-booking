package org.project.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.RedisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataRedisTest(properties = "spring.redis.port=6379")
@Import(RedisConfiguration.class)
public class RefreshTokenRepositoryTest {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  private RefreshTokenRepository refreshTokenRepository;
  private final Long refreshExpireTimeInSeconds = 100L;

  @BeforeEach
  void setUp() {
    refreshTokenRepository = new RefreshTokenRepository(redisTemplate, refreshExpireTimeInSeconds);
  }

  @AfterEach
  void tearDownAll() {
    redisTemplate.getConnectionFactory().getConnection().flushAll();
  }

  @DisplayName("save() 메소드는 refresh token을 키로, refresh token의 만료 시간을 값으로 저장한다.")
  @Test
  public void test_save() {
    // given
    String refreshToken = "testRefreshToken";

    // when
    refreshTokenRepository.save(refreshToken);

    // then
    // test set()
    Object result = redisTemplate.opsForValue().get(refreshToken);
    assertThat(result).isEqualTo(String.valueOf(refreshExpireTimeInSeconds));

    // test expire()
    Long expire = redisTemplate.getExpire(refreshToken);
    assertThat(expire).isEqualTo(refreshExpireTimeInSeconds);
  }

  @DisplayName("find() 메소드는 refresh token을 키로 하는 값을 찾아 반환한다.")
  @Test
  public void test_find() {
    // given
    String refreshToken = "testRefreshToken";
    refreshTokenRepository.save(refreshToken);

    // when
    Long result = refreshTokenRepository.find(refreshToken).get();

    // then
    assertThat(result).isEqualTo(refreshExpireTimeInSeconds);
  }

  @DisplayName("find() 메소드는 만료된 값은 찾을 수 없다.")
  @Test
  public void test_find_expired() {
    // given
    String refreshToken = "testRefreshToken";
    refreshTokenRepository.save(refreshToken);

    // when
    redisTemplate.expire(refreshToken, 0, TimeUnit.SECONDS);

    // then
    assertThat(refreshTokenRepository.find(refreshToken)).isEmpty();
  }

  @DisplayName("delete() 메소드는 refresh token을 키로 하는 값을 삭제한다.")
  @Test
  public void test_delete() {
    // given
    String refreshToken = "testRefreshToken";
    refreshTokenRepository.save(refreshToken);

    // when
    refreshTokenRepository.delete(refreshToken);

    // then
    assertThat(refreshTokenRepository.find(refreshToken)).isEmpty();
  }
}
