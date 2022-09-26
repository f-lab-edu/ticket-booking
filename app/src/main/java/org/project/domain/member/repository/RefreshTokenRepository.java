package org.project.domain.member.repository;

import org.project.domain.member.domain.Jwt;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

  public Jwt findOne(String refresh) {
    throw new UnsupportedOperationException(
        "refreshTokenRepository.findOne() not implemented yet.");
  }

}
