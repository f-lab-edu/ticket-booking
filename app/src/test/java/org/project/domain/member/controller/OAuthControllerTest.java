package org.project.domain.member.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
  void loginWithGoogle() throws Exception {
    String testAuthCode = "testAuthCode";
    when(mockOAuthService.loginWithGoogle(testAuthCode)).thenReturn(
        new AuthTokens("test access", "test refresh"));

    mockMvc.perform(get("/api/v1/oauth/google?code=" + testAuthCode)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access").exists())
        .andExpect(jsonPath("$.refresh").exists());
  }

}
