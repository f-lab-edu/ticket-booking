package org.project.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.common.domain.BaseTimeEntity;

@NoArgsConstructor()
@Table(name = "member")
@Entity
@Getter
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email;

  @Column(name = "provider", nullable = false, length = 20)
  private String provider;

  @Builder
  Member(String email, String provider) {
    this.email = email;
    this.provider = provider;
  }
}
