package org.project.concert.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.common.domain.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concerts")
@Entity
@Getter
public class Concert extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 80)
  private String title;

  @Column(length = 120)
  private String description;

  public Concert(String title, String description) {
    this.title = title;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Concert concert = (Concert) o;
    return id.equals(concert.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
