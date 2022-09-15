package org.project.domain.member.repository;

import java.util.HashMap;
import java.util.Map;
import org.project.domain.member.domain.Member;
import org.springframework.stereotype.Repository;

/**
 * 디비 연결 없이 메모리에 저장하기 위한 레포지토리
 */
@Repository
public class MemberRepository {

  private static final MemberRepository instance = new MemberRepository();
  private static Long sequence = 0L;
  // in-memory db
  private static final Map<Long, Member> store = new HashMap<>();

  public static MemberRepository getInstance() {
    return instance;
  }

  public Member save(Member member) {
    Member memberAlreadyExists = findByEmailAndProvider(member.getEmail(), member.getProvider());
    if (memberAlreadyExists != null) {
      return memberAlreadyExists;
    }

    member.setId(++sequence);
    store.put(member.getId(), member);
    return cloneMember(member);
  }

  public Member findById(Long id) {
    if (!store.containsKey(id)) {
      return null;
    }
    return cloneMember(store.get(id));
  }

  // find by email and OAuth provider
  public Member findByEmailAndProvider(String email, String provider) {
    Member member = store.values().stream()
        .filter(m -> m.getEmail().equals(email))
        .filter(m -> m.getProvider().equals(provider))
        .findAny()
        .orElse(null);

    if (member == null) {
      return null;
    }
    return cloneMember(member);
  }

  private static Member cloneMember(Member member) {
    return new Member(member.getId(), member.getEmail(), member.getProvider());
  }

  public void clearStore() {
    store.clear();
  }

  public Long getStoreSize() {
    return (long) store.size();
  }


}
