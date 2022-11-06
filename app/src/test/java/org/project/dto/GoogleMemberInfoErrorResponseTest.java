package org.project.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@JsonTest
@MockBean(JpaMetamodelMappingContext.class)
public class GoogleMemberInfoErrorResponseTest {

  @Autowired
  private JacksonTester<GoogleUserInfoErrorResponse> json;

  @Test
  public void testDeserialize() throws Exception {
    int code = 400;
    String message = "Invalid Value";
    String status = "INVALID_ARGUMENT";
    String content = "{\"error\":{\"code\":" + code + ",\"message\":\"" + message
        + "\",\"status\":\"" + status + "\"}}";

    GoogleUserInfoErrorResponse response = json.parseObject(content);

    assertThat(response.getCode()).isEqualTo(code);
    assertThat(response.getMessage()).isEqualTo(message);
    assertThat(response.getStatus()).isEqualTo(status);
  }
}
