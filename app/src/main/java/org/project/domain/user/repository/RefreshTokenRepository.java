package org.project.domain.user.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

  public Optional<Long> find(String refreshToken) {
    throw new UnsupportedOperationException("refreshTokenRepository.find() not implemented yet.");
  }

}
