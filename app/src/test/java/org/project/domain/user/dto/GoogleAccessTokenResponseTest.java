package org.project.domain.user.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@JsonTest
@MockBean(JpaMetamodelMappingContext.class)
public class GoogleAccessTokenResponseTest {

  @Autowired
  private JacksonTester<GoogleAccessTokenResponse> json;

  @Test
  public void testDeserialization() throws Exception {
    String accessToken = "access token";
    String tokenType = "token type";
    int expiresIn = 3600;
    String refreshToken = "refresh token";
    String scope = "scope";
    String content = "{\"access_token\":\"" + accessToken + "\",\"token_type\":\"" + tokenType
        + "\",\"expires_in\":" + expiresIn + ",\"refresh_token\":\"" + refreshToken
        + "\",\"scope\":\"" + scope + "\"}";

    GoogleAccessTokenResponse response = json.parseObject(content);

    assertThat(response.getAccessToken()).isEqualTo(accessToken);
    assertThat(response.getTokenType()).isEqualTo(tokenType);
    assertThat(response.getExpiresIn()).isEqualTo(expiresIn);
    assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
    assertThat(response.getScope()).isEqualTo(scope);
  }

}
