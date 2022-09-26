package org.project.domain.user.repository;

import org.project.domain.user.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {


  public User findOrCreateByEmailAndProvider(String email, String provider) {
    throw new UnsupportedOperationException(
        "userRepository.findByEmailAndProvider() not implemented yet.");
    // TODO: 이메일과 프로바이더가 일치하는 유저를 찾아서 반환
    // TODO: 이메일은 일치하지만 프로바이더가 일치하지 않는 경우 예외 처리
    // TODO: 이메일과 프로바이더가 일치하는 유저가 없는 경우 새로운 유저를 생성해서 반환
  }

  public User save(User user) {
    throw new UnsupportedOperationException("userRepository.save() not implemented yet.");
    // TODO: 유저를 저장하고 저장된 유저를 반환
  }
}
