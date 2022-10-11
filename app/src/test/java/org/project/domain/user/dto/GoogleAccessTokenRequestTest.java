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
public class GoogleAccessTokenRequestTest {

  @Autowired
  private JacksonTester<GoogleAccessTokenRequest> json;

  @Test
  public void testSerialization() throws Exception {
    String code = "code";
    String clientId = "clientId";
    String clientSecret = "clientSecret";
    String redirectUri = "redirectUri";
    String grantType = "authorization_code";
    GoogleAccessTokenRequest request = new GoogleAccessTokenRequest();
    request.setCode(code);
    request.setClientId(clientId);
    request.setClientSecret(clientSecret);
    request.setRedirectUri(redirectUri);
    request.setGrantType(grantType);

    String expectedJson = "{\"code\":\"" + code + "\",\"client_id\":\"" + clientId
        + "\",\"client_secret\":\"" + clientSecret + "\",\"redirect_uri\":\"" + redirectUri
        + "\",\"grant_type\":\"" + grantType + "\"}";

    assertThat(json.write(request)).isEqualToJson(expectedJson);
  }

}
