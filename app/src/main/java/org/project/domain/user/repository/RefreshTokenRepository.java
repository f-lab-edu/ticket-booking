package org.project.domain.user.repository;

import org.project.domain.user.domain.Jwt;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

  public Jwt findOne(String refresh) {
    throw new UnsupportedOperationException(
        "refreshTokenRepository.findOne() not implemented yet.");
  }

}
