package org.project.controller;

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
import org.project.dto.AuthLogoutRequest;
import org.project.dto.AuthTokens;
import org.project.dto.OAuthLoginResponse;
import org.project.dto.TokenRefreshRequest;
import org.project.dto.TokenRefreshResponse;
import org.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
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

  @Test
  @DisplayName("Authorization Code 요청에 대한 리다이렉션이 error를 포함하면 에러 바디로 응답한다.")
  void loginWithGoogleError() throws Exception {
    // given
    String error = "testError";
    String errorMessage = String.format("Authorization code request failed with error: %s", error);

    // when
    ResultActions result = mockMvc.perform(
        get("/api/v1/oauth/google")
            .param("error", error)
    );

    // then
    result.andExpect(status().isBadRequest());
    result.andExpect(content().json(
        "{\"error\":\"" + HttpStatus.BAD_REQUEST.getReasonPhrase()
            + "\",\"status\":" + HttpStatus.BAD_REQUEST.value()
            + ",\"message\":\"" + errorMessage + "\"}"
    ));
  }

  @Test
  @DisplayName("code, error 모두 없는 경우 400 에러를 응답한다.")
  void loginWithGoogleNoCodeNoError() throws Exception {
    // given
    String errorMessage = "Code or error parameter is required.";

    // when
    ResultActions result = mockMvc.perform(
        get("/api/v1/oauth/google")
    );

    // then
    result.andExpect(status().isBadRequest());
    result.andExpect(content().json(
        String.format("{\"error\":\"%s\",\"status\":%d,\"message\":\"%s\"}",
            HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), errorMessage)
    ));

  }

  @Test
  @DisplayName("토큰 리프레시 요청에는 body에 refresh 토큰이 필요하다.")
  void refreshAccessToken_postBody_test() throws Exception {
    // given
    final String url = "/api/v1/oauth/refresh";

    // when
    final ResultActions result = mockMvc.perform(
        post(url)
            .content("{}")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    result.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("토큰 리프레시 요청 body에 refresh토큰이 있고, 정상적인 경우 200 OK를 반환한다.")
  void refreshAccessToken_success_case() throws Exception {
    // given
    final String url = "/api/v1/oauth/refresh";
    final String refreshToken = "testRefreshToken";
    final String accessToken = "testAccessToken";
    final TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshToken);
    final TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(accessToken);
    given(authService.refreshAccessToken(refreshToken)).willReturn(accessToken);

    // when
    final ResultActions result = mockMvc.perform(
        post(url)
            .content(convertObjectToJsonString(tokenRefreshRequest))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    then(authService).should().refreshAccessToken(refreshToken);
    result.andExpect(status().isOk());
    result.andExpect(content().json(convertObjectToJsonString(tokenRefreshResponse)));
  }


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
