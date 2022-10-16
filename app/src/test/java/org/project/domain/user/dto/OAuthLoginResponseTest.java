package org.project.domain.user.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OAuthLoginResponseTest {

  @Test
  @DisplayName("AuthTokens를 받으면 동일한 access, refresh를 가진 OAuthLoginResponse를 반환한다.")
  void testConstructor() {
    // given
    String accessToken = "accessToken";
    String refreshToken = "refreshToken";
    AuthTokens authTokens = new AuthTokens(accessToken, refreshToken);

    // when
    OAuthLoginResponse oAuthLoginResponse = new OAuthLoginResponse(authTokens);

    // then
    assertThat(oAuthLoginResponse.getAccess()).isEqualTo(accessToken);
    assertThat(oAuthLoginResponse.getRefresh()).isEqualTo(refreshToken);
  }
}
