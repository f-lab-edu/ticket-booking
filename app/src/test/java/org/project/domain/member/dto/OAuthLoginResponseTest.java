package org.project.domain.member.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OAuthLoginResponseTest {

  @Test
  @DisplayName("AuthTokens를 받으면 동일한 access, refresh를 가진 OAuthLoginResponse를 반환한다.")
  void testConstructor() {
    // given
    String access = "access";
    String refresh = "refresh";
    AuthTokens authTokens = new AuthTokens(access, refresh);

    // when
    OAuthLoginResponse oAuthLoginResponse = new OAuthLoginResponse(authTokens);

    // then
    assertThat(oAuthLoginResponse.getAccess()).isEqualTo("access");
    assertThat(oAuthLoginResponse.getRefresh()).isEqualTo("refresh");
  }
}