package org.project.domain.user.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.domain.user.dto.AuthLogoutRequest;
import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.dto.OAuthLoginResponse;
import org.project.domain.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;


  @Test
  @DisplayName("쿼리 파라미터로 code만 받는 경우 해당 code로 AuthService.loginWithGoogle()을 호출한다.")
  void loginWithGoogle() throws Exception {
    // given
    String code = "testCode";
    String accessToken = "testAccessToken";
    String refreshToken = "testRefreshToken";
    AuthTokens authTokens = new AuthTokens(accessToken, refreshToken);
    OAuthLoginResponse oAuthLoginResponse = new OAuthLoginResponse(authTokens);
    given(authService.loginWithGoogle(code)).willReturn(authTokens);

    // when
    ResultActions result = mockMvc.perform(
        get("/api/v1/oauth/google")
            .param("code", code)
    );

    // then
    then(authService).should().loginWithGoogle(code);
    result.andExpect(status().isOk());
    result.andExpect(content().json(convertObjectToJsonString(oAuthLoginResponse)));
  }

  // test logout
  @Test
  @DisplayName("로그아웃 요청이 들어오면 받은 refresh token으로 AuthService.logout()을 호출한다.")
  void logout() throws Exception {
    // given
    String refreshToken = "testRefreshToken";
    AuthLogoutRequest authLogoutRequest = new AuthLogoutRequest();
    authLogoutRequest.setRefresh(refreshToken);
    String content = convertObjectToJsonString(authLogoutRequest);

    // when
    ResultActions result = mockMvc.perform(
        post("/api/v1/oauth/logout")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    then(authService).should().logout(refreshToken);
    result.andExpect(status().isOk());
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
