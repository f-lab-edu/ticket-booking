package org.project.domain.user.repository;

import java.util.Optional;
import org.project.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  // find by email and provider
  Optional<Member> findByEmailAndProvider(String email, String provider);
}
