package org.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.configuration.RedisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataRedisTest(properties = "spring.redis.port=6379")
@Import(RedisConfiguration.class)
public class RedisTemplateTest {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @AfterEach
  void tearDownAll() {
    redisTemplate.getConnectionFactory().getConnection().flushAll();
  }

  @Test
  void testRedisTemplate() {
    String key = "testKey";
    String value = "testValue";
    Long expireTime = 100L;
    redisTemplate.opsForValue().set(key, value);
    redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);

    // test set()
    Object result = redisTemplate.opsForValue().get(key);
    assertThat(result).isEqualTo(value);

    // test expire()
    Long expire = redisTemplate.getExpire(key);
    assertThat(expire).isLessThanOrEqualTo(expireTime);

    // test delete()
    redisTemplate.delete(key);
    assertThat(redisTemplate.hasKey(key)).isFalse();
  }
}
