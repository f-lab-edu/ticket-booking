package org.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.configuration.JwtProperties;
import org.project.configuration.RedisConfiguration;
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
  private final JwtProperties jwtProperties
      = new JwtProperties(
      "dummy-access-secret",
      "dummy-refresh-secret",
      10L,
      100L
  );

  @BeforeEach
  void setUp() {
    refreshTokenRepository = new RefreshTokenRepository(redisTemplate, jwtProperties);
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
    // check existence
    assertThat(redisTemplate.hasKey(refreshToken)).isTrue();
    // test expire() approximately
    assertThat(redisTemplate.getExpire(refreshToken, TimeUnit.SECONDS))
        .isBetween(90L, 100L);
  }

  @DisplayName("exists() 메소드는 repository에 존재하는 key에 대해서만 true를 반환.")
  @Test
  public void testThatExistsMethodReturnsTrueForKeyWhichExists() {
    // given
    String refreshToken = "testRefreshToken";
    refreshTokenRepository.save(refreshToken);

    // when
    Boolean mustBeTrueResult = refreshTokenRepository.exists(refreshToken);
    Boolean mustBeFalseResult = refreshTokenRepository.exists("nonexistentKey");

    // then
    assertThat(mustBeTrueResult).isTrue();
    assertThat(mustBeFalseResult).isFalse();
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
    assertThat(redisTemplate.hasKey(refreshToken)).isFalse();
  }
}
