package org.project.domain.user.service;

import org.project.domain.user.dto.AuthTokens;
import org.project.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final OAuthGrantService oAuthGrantService;
  private final LoginCommonService loginCommonService;

  public AuthService(UserRepository userRepository, OAuthGrantService oAuthGrantService,
      LoginCommonService loginCommonService) {
    this.userRepository = userRepository;
    this.oAuthGrantService = oAuthGrantService;
    this.loginCommonService = loginCommonService;
  }

  public AuthTokens loginWithGoogle(String code) {
    throw new UnsupportedOperationException("loginWithGoogle() not implemented yet.");

    // TODO: get email from google
//    String email = oAuthGrantService.getGoogleEmail(code);

    // TODO: find or create user
//    User user = userRepository.findOrCreateByEmailAndProvider(email, "google");

    // TODO: login user
//    AuthTokens authTokens = loginCommonService.loginUser(user);

//    return authTokens;
  }
}
