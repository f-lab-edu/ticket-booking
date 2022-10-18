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
public class GoogleAccessTokenErrorResponseTest {

  @Autowired
  private JacksonTester<GoogleAccessTokenErrorResponse> json;

  @Test
  public void testDeserialize() throws Exception {
    String error = "invalid_grant";
    String errorDescription = "Invalid Value";
    String content =
        "{\"error\":\"" + error + "\",\"error_description\":\"" + errorDescription + "\"}";

    GoogleAccessTokenErrorResponse response = json.parseObject(content);

    assertThat(response.getError()).isEqualTo(error);
    assertThat(response.getErrorDescription()).isEqualTo(errorDescription);
  }

}
