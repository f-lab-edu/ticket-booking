package org.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Table(name = "ticket")
@Entity
@Getter
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
}
