package org.project.repository;

import java.util.Optional;
import org.project.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

  Optional<Concert> findById(Long id);
}
