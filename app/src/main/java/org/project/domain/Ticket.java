package org.project.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Table(name = "ticket")
@Entity
@Getter
@Setter
public class Ticket extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "concert_id")
  @NotNull
  private Concert concert;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private TicketOrder ticketOrder;

  // locked until
  @Column(name = "locked_until")
  private LocalDateTime lockedUntil;

  @ManyToOne
  @JoinColumn(name = "locked_by")
  private Member lockedBy;
}
