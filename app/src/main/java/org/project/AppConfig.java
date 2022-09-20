package org.project;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.create();
  }


  @Bean(name = "refreshTokens")
  Map<String, Boolean> refreshTokens() {
    return new ConcurrentHashMap<>();
  }
}
