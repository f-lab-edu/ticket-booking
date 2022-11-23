package org.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.LockModeType;
import org.project.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Ticket t WHERE t.id = :ids AND t.lockedUntil > :lockedUntil")
  List<Ticket> findAllByIdAndLockedUntilAfterWithLock(@Param("ids") List<Long> ids,
      @Param("lockedUntil") LocalDateTime lockedUntil);


  @Modifying(clearAutomatically = true)
  @Query("UPDATE Ticket t SET t.lockedBy = :memberId, t.lockedUntil = :lockedUntil WHERE t.id IN :ids")
  int lockTickets(@Param("memberId") Long memberId, @Param("ids") List<Long> ids,
      @Param("lockedUntil") LocalDateTime lockedUntil);

}
