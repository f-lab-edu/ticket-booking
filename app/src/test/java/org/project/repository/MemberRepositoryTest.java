package org.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("유저 저장")
  public void save() {
    String email = "test@test.com";
    String provider = "google";
    Member member = Member.builder()
        .email(email)
        .provider(provider)
        .build();

    memberRepository.save(member);

    Member savedMember = memberRepository.findByEmailAndProvider(email, provider).get();

    assertThat(savedMember.getId()).isNotNull();
    assertThat(savedMember.getEmail()).isEqualTo(email);
    assertThat(savedMember.getProvider()).isEqualTo(provider);
    assertThat(savedMember.getCreatedDateTime()).isNotNull();
  }
}
