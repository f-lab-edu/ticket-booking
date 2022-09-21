package org.project.domain.member.service;

import io.jsonwebtoken.security.Keys;
import java.util.Map;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.domain.member.domain.Member;
import org.project.domain.member.dto.AuthTokens;
import org.project.domain.member.dto.GoogleUserInfoResponse;
import org.project.domain.member.dto.KakaoAccount;
import org.project.domain.member.dto.KakaoUserInfoResponse;
import org.project.domain.member.dto.OAuthAccessTokenResponse;
import org.project.domain.member.repository.MemberRepository;
import org.project.util.JwtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OAuthServiceTest {

  // member repository stub
  private final MemberRepository memberRepository = mock(MemberRepository.class);

  private final OAuthWebClientService oAuthWebClientService = mock(OAuthWebClientService.class);

  private final JwtService jwtService = mock(JwtService.class);

  private final Map<String, Boolean> mockRefreshTokens = mock(Map.class);

  private final String googleClientId = "googleClientId";
  private final String googleClientSecret = "googleClientSecret";
  private final String googleRedirectUri = "googleRedirectUri";
  private final String kakaoClientId = "kakaoClientId";
  private final String kakaoClientSecret = "kakaoClientSecret";
  private final String kakaoRedirectUri = "kakaoRedirectUri";
  private final String accessSecret = "accessSecret";
  private final String refreshSecret = "refreshSecret";
  private final Long accessExpiration = 1000L;
  private final Long refreshExpiration = 2000L;

  private final OAuthService oAuthService = new OAuthService(
      oAuthWebClientService,
      jwtService,
      memberRepository,
      mockRefreshTokens,
      googleClientId,
      googleClientSecret,
      googleRedirectUri,
      kakaoClientId,
      kakaoClientSecret,
      kakaoRedirectUri,
      accessSecret,
      refreshSecret,
      accessExpiration,
      refreshExpiration

  );


  void mockAllForGoogle() {

    given(oAuthWebClientService.getGoogleAccessToken(anyString(), anyString(), anyString(),
        anyString())).willReturn(OAuthAccessTokenResponse.builder().accessToken("").build());
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email("").build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(
        Member.builder().id(10L).email("").provider("").build());
    given(memberRepository.save(any())).willReturn(
        Member.builder().id(21L).email("").provider("").build());
    given(jwtService.generateToken(any(), anyString(), any())).willReturn("token");
    given(mockRefreshTokens.put(anyString(), anyBoolean())).willReturn(true);
  }

  void mockAllForKaKao() {

    given(oAuthWebClientService.getKakaoAccessToken(anyString(), anyString(), anyString(),
        anyString())).willReturn(OAuthAccessTokenResponse.builder().accessToken("").build());
    given(oAuthWebClientService.getKakaoUserInfo(anyString())).willReturn(
        KakaoUserInfoResponse.builder().kakaoAccount(
            KakaoAccount.builder().email("").build()).build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(
        Member.builder().id(10L).email("").provider("").build());
    given(memberRepository.save(any())).willReturn(
        Member.builder().id(21L).email("").provider("").build());
    given(jwtService.generateToken(any(), anyString(), any())).willReturn("token");
    given(mockRefreshTokens.put(anyString(), anyBoolean())).willReturn(true);
  }

  @Test
  @DisplayName("loginWithGoogle은 null code를 받는 경우 IllegalArgumentException을 던진다.")
  public void loginWithGoogle_throwsIllegalArgumentException_whenNullCodeIsGiven() {
    // given
    String code = null;

    // when
    Throwable throwable = catchThrowable(() -> oAuthService.loginWithGoogle(code));

    // then
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("loginWithGoogle은 주입받은 client id, client secret, redirect uri, 요청 받은 code를 이용해 google api에 요청을 보낸다.")
  public void loginWithGoogle_callsGetGoogleAccessToken_withGivenVariables() {
    // given
    String code = "code";
    mockAllForGoogle();
    GoogleUserInfoResponse test = oAuthWebClientService.getGoogleUserInfo("access_token");
    System.out.println("tetst = " + test);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithGoogle(code);
    }

    // then
    verify(oAuthWebClientService).getGoogleAccessToken(
        code,
        googleClientId,
        googleClientSecret,
        googleRedirectUri
    );
  }

  @Test
  @DisplayName("loginWithGoogle은 구글에서 응답받은 access token을 이용해 getGoogleUserInfo를 호출한다")
  public void getGoogleEmail_callsGetGoogleUserInfo_withGivenAccessToken() {
    // given
    String accessToken = "access_token";
    mockAllForGoogle();
    given(oAuthWebClientService.getGoogleAccessToken(anyString(), anyString(), anyString(),
        anyString())).willReturn(
        OAuthAccessTokenResponse.builder().accessToken(accessToken).build());

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithGoogle("code");
    }

    // then
    verify(oAuthWebClientService).getGoogleUserInfo(accessToken);
  }

  @Test
  @DisplayName("loginWithGoogle은 구글에서 응답받은 email을 이용해 memberRepository의 findByEmailAndProvider를 호출한다.")
  public void getGoogleEmail_callsFindByEmailAndProvider_withGivenEmail() {
    // given
    String email = "email";
    mockAllForGoogle();
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email(email).build());

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithGoogle("code");
    }

    // then
    verify(memberRepository).findByEmailAndProvider(email, "google");
  }

  @Test
  @DisplayName("loginWithGoogle은 받은 email의 유저가 memberRepository에 존재하지 않는 경우 memberRepository의 save를 호출한다.")
  public void getGoogleEmail_callsSave_whenUserDoesNotExist() {
    // given
    String email = "email";
    mockAllForGoogle();
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email(email).build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(null);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithGoogle("code");
    }

    // then
    verify(memberRepository).save(any());
  }

  @Test
  @DisplayName("loginWithGoogle은 받은 email의 유저가 memberRepository에 존재하는 경우 memberRepository의 save를 호출하지 않는다.")
  public void getGoogleEmail_doesNotCallSave_whenUserExists() {
    // given
    String email = "email";
    mockAllForGoogle();
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email(email).build());
    given(memberRepository.findByEmailAndProvider(email, "google")).willReturn(
        Member.builder().id(10L).email("").provider("").build());

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithGoogle("code");
    }

    // then
    verify(memberRepository, never()).save(any());
  }

  @Test
  @DisplayName("loginWithGoogle은 적절한 파라미터로 generateToken을 호출한다.")
  public void getGoogleEmail_callsGenerateToken_withProperParameters() {
    // given
    mockAllForGoogle();
    String email = "email";
    Member member = Member.builder().id(10L).email(email).provider("google").build();
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email(email).build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(member);
    SecretKey accessSecretKey = mock(SecretKey.class);
    SecretKey refreshSecretKey = mock(SecretKey.class);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any()))
          .thenReturn(accessSecretKey)
          .thenReturn(refreshSecretKey);
      oAuthService.loginWithGoogle("code");
    }

    // then
    verify(jwtService).generateToken(eq(accessSecretKey), eq(member.getId().toString()), any());
    verify(jwtService).generateToken(eq(refreshSecretKey), eq(member.getId().toString()), any());
  }

  @Test
  @DisplayName("loginWithGoogle은 generateToken에서 반환한 refreshToken을 refreshTokens에 저장한다.")
  public void getGoogleEmail_savesRefreshToken() {
    // given
    mockAllForGoogle();
    String email = "email";
    Member member = Member.builder().id(10L).email(email).provider("google").build();
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email(email).build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(member);
    SecretKey accessSecretKey = mock(SecretKey.class);
    SecretKey refreshSecretKey = mock(SecretKey.class);
    String refreshToken = "refresh_token";
    given(jwtService.generateToken(any(), any(), any())).willReturn(refreshToken);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any()))
          .thenReturn(accessSecretKey)
          .thenReturn(refreshSecretKey);
      oAuthService.loginWithGoogle("code");
    }

    // then
    verify(mockRefreshTokens).put(refreshToken, true);
  }

  @Test
  @DisplayName("loginWithGoogle은 유저의 정보로 생성된 AuthTokens를 반환한다.")
  public void getGoogleEmail_returnsAuthTokens() {
    // given
    mockAllForGoogle();
    String email = "email";
    Member member = Member.builder().id(10L).email(email).provider("google").build();
    given(oAuthWebClientService.getGoogleUserInfo(anyString())).willReturn(
        GoogleUserInfoResponse.builder().email(email).build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(member);
    SecretKey accessSecretKey = mock(SecretKey.class);
    SecretKey refreshSecretKey = mock(SecretKey.class);
    String accessToken = "access_token";
    String refreshToken = "refresh_token";
    given(jwtService.generateToken(any(), any(), any())).willReturn(accessToken, refreshToken);

    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      // when
      utilities.when(() -> Keys.hmacShaKeyFor(any()))
          .thenReturn(accessSecretKey)
          .thenReturn(refreshSecretKey);
      AuthTokens authTokens = oAuthService.loginWithGoogle("code");

      // then
      assertThat(authTokens.getAccess()).isEqualTo(accessToken);
      assertThat(authTokens.getRefresh()).isEqualTo(refreshToken);
    }
  }

  @Test
  @DisplayName("loginWithKakao는 null code를 받으면 IllegalArgumentException을 던진다.")
  public void loginWithKakao_throwsIllegalArgumentException_whenNullCode() {
    // given
    String code = null;

    // when & then
    assertThatThrownBy(() -> oAuthService.loginWithKakao(code))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("loginWithKakao는 받은 code로 oAuthWebClientService의 getKakaoAccessToken을 호출한다.")
  public void loginWithKakao_callsGetKakaoAccessToken() {
    // given
    mockAllForKaKao();
    String code = "code";

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(oAuthWebClientService).getKakaoAccessToken(code, kakaoClientId, kakaoClientSecret,
        kakaoRedirectUri);
  }

  @Test
  @DisplayName("loginWithKakao는 oAuthWebClientService의 getKakaoAccessToken에서 반환한 OAuthAccessTokenResponse로 getKakaoUserInfo를 호출한다.")
  public void loginWithKakao_callsGetKakaoUserInfo() {
    // given
    mockAllForKaKao();
    String code = "code";
    String kakaoAccessToken = "kakao_access_token";

    given(oAuthWebClientService.getKakaoAccessToken(anyString(), anyString(), anyString(),
        anyString()))
        .willReturn(OAuthAccessTokenResponse.builder()
            .accessToken(kakaoAccessToken)
            .build());

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(oAuthWebClientService).getKakaoUserInfo(kakaoAccessToken);
  }

  @Test
  @DisplayName("loginWithKakao는 받은 email을 이용해 memberRepository의 findByEmailAndProvider를 호출한다.")
  public void loginWithKakao_callsFindByEmailAndProvider() {
    // given
    mockAllForKaKao();
    String code = "code";
    String email = "email";

    given(oAuthWebClientService.getKakaoUserInfo(anyString()))
        .willReturn(KakaoUserInfoResponse.builder()
            .kakaoAccount(KakaoAccount.builder()
                .email(email).build())
            .build());

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(memberRepository).findByEmailAndProvider(email, "kakao");
  }

  @Test
  @DisplayName("loginWithKakao는 memberRepository의 findByEmailAndProvider에서 반환한 Member가 null이면 memberRepository의 save를 호출한다.")
  public void loginWithKakao_callsSave() {
    // given
    mockAllForKaKao();
    String code = "code";
    String email = "email";

    given(oAuthWebClientService.getKakaoUserInfo(anyString()))
        .willReturn(KakaoUserInfoResponse.builder()
            .kakaoAccount(KakaoAccount.builder()
                .email(email).build())
            .build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(null);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(memberRepository).save(any());
  }

  @Test
  @DisplayName("loginWithKakao는 memberRepository의 findByEmailAndProvider에서 반환한 Member가 null이 아니면 memberRepository의 save를 호출하지 않는다.")
  public void loginWithKakao_doesNotCallSave() {
    // given
    mockAllForKaKao();
    String code = "code";
    String email = "email";

    given(oAuthWebClientService.getKakaoUserInfo(anyString()))
        .willReturn(KakaoUserInfoResponse.builder()
            .kakaoAccount(KakaoAccount.builder()
                .email(email).build())
            .build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString()))
        .willReturn(Member.builder().id(30L).email(email).provider("kakao").build());

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(mock(SecretKey.class));
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(memberRepository, never()).save(any());
  }

  @Test
  @DisplayName("loginWithKakao는 적절한 파라미터로 generateToken을 호출한다.")
  public void loginWithKakao_callsGenerateToken() {
    // given
    mockAllForKaKao();
    String code = "code";
    String email = "email";
    Member member = Member.builder().id(30L).email(email).provider("kakao").build();

    given(oAuthWebClientService.getKakaoUserInfo(anyString()))
        .willReturn(KakaoUserInfoResponse.builder()
            .kakaoAccount(KakaoAccount.builder()
                .email(email).build())
            .build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(member);
    SecretKey accessSecretKey = mock(SecretKey.class);
    SecretKey refreshSecretKey = mock(SecretKey.class);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any()))
          .thenReturn(accessSecretKey)
          .thenReturn(refreshSecretKey);
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(jwtService).generateToken(eq(accessSecretKey), eq(member.getId().toString()), any());
    verify(jwtService).generateToken(eq(refreshSecretKey), eq(member.getId().toString()), any());
  }

  @Test
  @DisplayName("loginWithKakao는 generateToken에서 반환한 refreshToken을 refreshTokens에 저장한다.")
  public void loginWithKakao_savesRefreshToken() {
    // given
    mockAllForKaKao();
    String code = "code";
    String email = "email";
    Member member = Member.builder().id(30L).email(email).provider("kakao").build();
    given(oAuthWebClientService.getKakaoUserInfo(anyString()))
        .willReturn(KakaoUserInfoResponse.builder()
            .kakaoAccount(KakaoAccount.builder()
                .email(email).build())
            .build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(member);
    SecretKey accessSecretKey = mock(SecretKey.class);
    SecretKey refreshSecretKey = mock(SecretKey.class);
    String refreshToken = "refresh_token";
    given(jwtService.generateToken(any(), any(), any())).willReturn(refreshToken);

    // when
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any()))
          .thenReturn(accessSecretKey)
          .thenReturn(refreshSecretKey);
      oAuthService.loginWithKakao(code);
    }

    // then
    verify(mockRefreshTokens).put(refreshToken, true);
  }

  @Test
  @DisplayName("loginWithKakao는 유저의 정보로 생성된 AuthTokens를 반환한다.")
  public void loginWithKakao_returnsAuthTokens() {
    // given
    mockAllForKaKao();
    String code = "code";
    String email = "email";
    Member member = Member.builder().id(30L).email(email).provider("kakao").build();
    given(oAuthWebClientService.getKakaoUserInfo(anyString()))
        .willReturn(KakaoUserInfoResponse.builder()
            .kakaoAccount(KakaoAccount.builder()
                .email(email).build())
            .build());
    given(memberRepository.findByEmailAndProvider(anyString(), anyString())).willReturn(member);
    SecretKey accessSecretKey = mock(SecretKey.class);
    SecretKey refreshSecretKey = mock(SecretKey.class);
    String accessToken = "access_token";
    String refreshToken = "refresh_token";
    given(jwtService.generateToken(any(), any(), any()))
        .willReturn(accessToken)
        .willReturn(refreshToken);

    // when
    AuthTokens authTokens;
    try (MockedStatic<Keys> utilities = mockStatic(Keys.class)) {
      utilities.when(() -> Keys.hmacShaKeyFor(any()))
          .thenReturn(accessSecretKey)
          .thenReturn(refreshSecretKey);
      authTokens = oAuthService.loginWithKakao(code);
    }

    // then
    assertThat(authTokens.getAccess()).isEqualTo(accessToken);
    assertThat(authTokens.getRefresh()).isEqualTo(refreshToken);
  }

}
