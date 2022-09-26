package org.project.domain.user.service;

import org.springframework.stereotype.Service;

@Service
public class OAuthGrantService {

  String getGoogleEmail(String code) {
    throw new UnsupportedOperationException("getGoogleEmail() not implemented yet.");
    // TODO: WebClient를 이용하여 Google API Access Token을 발급

    // TODO: 발급받은 Access Token을 통해 유저의 이메일을 받아온다.

    // TODO: 위의 과정 중 정상 프로세스를 벗어나는 경우 예외 던지기
  }
}
