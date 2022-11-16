package org.project.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @OneToMany(mappedBy = "member")
  private List<TicketOrder> ticketOrders;

  public Member(String email, String provider) {
    this.email = email;
    this.provider = provider;
  }
}
