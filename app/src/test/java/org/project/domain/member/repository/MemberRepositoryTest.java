package org.project.domain.member.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.project.domain.member.domain.Member;

public class MemberRepositoryTest {

  MemberRepository memberRepository = MemberRepository.getInstance();

  @AfterEach
  void clearAll() {
    memberRepository.clearStore();
  }

  @Test
  void getInstance_should_return_not_null() {
    MemberRepository memberRepository = MemberRepository.getInstance();
    assertThat(memberRepository).isNotNull();
  }


  @Test
  void save_should_increase_store_size() {
    // given
    assertThat(memberRepository.getStoreSize()).isEqualTo(0);
    Member member = new Member("email", "provider");

    // when
    memberRepository.save(member);

    // then
    assertThat(memberRepository.getStoreSize()).isEqualTo(1);
  }

  @Test
  void save_should_return_saved_member() {
    // given
    String email = "email";
    String provider = "provider";
    Member member = new Member(email, provider);

    // when
    Member savedMember = memberRepository.save(member);

    // then
    assertThat(savedMember.getEmail()).isEqualTo(email);
    assertThat(savedMember.getProvider()).isEqualTo(provider);
  }

  @Test
  void findById_should_return_member() {
    // given
    String email = "email";
    String provider = "provider";
    Member member = new Member(email, provider);
    Member savedMember = memberRepository.save(member);

    // when
    Member findMember = memberRepository.findById(savedMember.getId());

    // then
    assertThat(findMember).isEqualTo(savedMember);
  }

  @Test
  void findById_should_return_null_with_invalid_id() {
    // given
    String email = "email";
    String provider = "provider";
    Member member = new Member(email, provider);
    memberRepository.save(member);

    // when
    Member findMember = memberRepository.findById(-1L);

    // then
    assertThat(findMember).isNull();
  }

  @Test
  void findByEmailAndProvider_should_return_member_with_exist_email_and_provider() {
    // given
    String email = "email";
    String provider = "provider";
    Member member = new Member(email, provider);
    Member savedMember = memberRepository.save(member);

    // when
    Member findMember = memberRepository.findByEmailAndProvider(email, provider);

    // then
    assertThat(findMember).isEqualTo(savedMember);
  }

  @Test
  void findByEmailAndProvider_should_return_null_with_the_same_email_but_different_provider() {
    // given
    String email = "email";
    String provider = "provider";
    Member member = new Member(email, provider);
    memberRepository.save(member);

    // when
    Member findMember = memberRepository.findByEmailAndProvider(email, "differentProvider");

    // then
    assertThat(findMember).isNull();
  }

  @Test
  void findByEmailAndProvider_should_return_null_with_the_same_provider_but_different_email() {
    // given
    String email = "email";
    String provider = "provider";
    Member member = new Member(email, provider);
    memberRepository.save(member);

    // when
    Member findMember = memberRepository.findByEmailAndProvider("differentEmail", provider);

    // then
    assertThat(findMember).isNull();
  }


  @Test
  void findByEmailAndProvider_should_return_null_with_not_exist_member() {
    // given
    String email = " ";
    String provider = " ";
    assertThat(memberRepository.getStoreSize()).isEqualTo(0);

    // when
    Member findMember = memberRepository.findByEmailAndProvider(email, provider);

    // then
    assertThat(findMember).isNull();
  }


}
