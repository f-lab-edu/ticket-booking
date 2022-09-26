package org.project.domain.member.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.domain.member.domain.Jwt;
import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.dto.OAuthLoginResponse;
import org.project.domain.member.service.OAuthLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = OAuthLoginController.class)
public class OAuthLoginControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OAuthLoginService oAuthLoginService;


  @Test
  @DisplayName("쿼리 파라미터로 code만 받는 경우 해당 code로 OAuthLoginService.loginWithGoogle()을 호출한다.")
  void loginWithGoogle() throws Exception {
    // given
    String code = "testCode";
    Jwt accessToken = new Jwt(null, null, null);
    Jwt refreshToken = new Jwt(null, null, null);
    AuthTokens authTokens = new AuthTokens(accessToken, refreshToken);
    OAuthLoginResponse oAuthLoginResponse = new OAuthLoginResponse(authTokens);
    given(oAuthLoginService.loginWithGoogle(code)).willReturn(authTokens);

    // when
    ResultActions result = mockMvc.perform(
        get("/api/v1/oauth/google")
            .param("code", code)
    );

    // then
    then(oAuthLoginService).should().loginWithGoogle(code);
    result.andExpect(status().isOk());
    result.andExpect(content().json(convertObjectToJsonString(oAuthLoginResponse)));
  }

  // TODO: 익셉션 핸들러 추가 후 (code: O, error: O), (code: X, error: O), (code: X, error: X) 각 케이스 테스트


  private String convertObjectToJsonString(Object o) {
    ObjectMapper mapper = new ObjectMapper();
    String jsonStr = null;
    try {
      jsonStr = mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return jsonStr;
  }
}
