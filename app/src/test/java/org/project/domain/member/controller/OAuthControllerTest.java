package org.project.domain.member.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OAuthController.class)
public class OAuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OAuthService mockOAuthService;

  @Test
  @DisplayName("구글 로그인 api는 쿼리스트링으로 받은 code를 서비스에게 전달한다.")
  void loginWithGoogle() throws Exception {
    String testAuthCode = "testAuthCode";
    when(mockOAuthService.loginWithGoogle(testAuthCode)).thenReturn(
        new AuthTokens("test access", "test refresh"));

    mockMvc.perform(get("/api/v1/oauth/google?code=" + testAuthCode)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access").exists())
        .andExpect(jsonPath("$.refresh").exists());
    verify(mockOAuthService).loginWithGoogle(testAuthCode);
  }

  @Test
  @DisplayName("카카오 로그인 api는 쿼리스트링으로 받은 code를 서비스에게 전달한다.")
  void loginWithKakao() throws Exception {
    String testAuthCode = "testAuthCode";
    when(mockOAuthService.loginWithKakao(testAuthCode)).thenReturn(
        new AuthTokens("test access", "test refresh"));

    mockMvc.perform(get("/api/v1/oauth/kakao?code=" + testAuthCode)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access").exists())
        .andExpect(jsonPath("$.refresh").exists());
    verify(mockOAuthService).loginWithKakao(testAuthCode);
  }

}
